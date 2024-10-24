package com.euphony.streaming.service.implementation;

import com.euphony.streaming.dto.request.PlaylistRequestDTO;
import com.euphony.streaming.dto.response.PlaylistResponseDTO;
import com.euphony.streaming.entity.CancionEntity;
import com.euphony.streaming.entity.PlaylistCancionEntity;
import com.euphony.streaming.entity.PlaylistEntity;
import com.euphony.streaming.entity.UsuarioEntity;
import com.euphony.streaming.exception.custom.*;
import com.euphony.streaming.repository.*;
import com.euphony.streaming.service.interfaces.IPlaylistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlaylistServiceImpl implements IPlaylistService {

    private final PlaylistRepository playlistRepository;
    private final UsuarioRepository usuarioRepository;
    private final CancionRepository cancionRepository;
    private final PlaylistCancionRepository playlistCancionRepository;

    @Override
    public List<PlaylistResponseDTO> findAllPlaylists() {
        try {
            log.info("Iniciando búsqueda de todas las playlists");
            List<PlaylistResponseDTO> playlists = playlistRepository.findAll().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            log.info("Búsqueda completada. Se encontraron {} playlists", playlists.size());
            return playlists;
        } catch (DataAccessException e) {
            log.error("Error al acceder a la base de datos durante la búsqueda de playlists: {}", e.getMessage());
            throw new PlaylistCreationException("Error al obtener las playlists", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("Error inesperado al buscar playlists: {}", e.getMessage());
            throw new PlaylistCreationException("Error inesperado al buscar playlists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public PlaylistResponseDTO findPlaylistById(Long id) {
        try {
            log.info("Iniciando búsqueda de playlist con ID: {}", id);
            return playlistRepository.findById(id)
                    .map(playlist -> {
                        log.info("Playlist encontrada: {} (ID: {})", playlist.getNombre(), playlist.getIdPlaylist());
                        return convertToDTO(playlist);
                    })
                    .orElseThrow(() -> {
                        log.error("No se encontró la playlist con ID: {}", id);
                        return new PlaylistNotFoundException("No se encontró la playlist con ID: " + id);
                    });
        } catch (PlaylistNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al buscar playlist con ID {}: {}", id, e.getMessage());
            throw new PlaylistCreationException("Error al buscar la playlist", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public void createPlaylist(PlaylistRequestDTO playlistRequestDTO) {
        try {
            log.info("Iniciando creación de playlist para usuario ID: {}", playlistRequestDTO.getUserId());

            UsuarioEntity usuario = usuarioRepository.findById(playlistRequestDTO.getUserId())
                    .orElseThrow(() -> {
                        log.error("No se encontró el usuario con ID: {}", playlistRequestDTO.getUserId());
                        return new UserNotFoundException("No se encontró el usuario con ID: " + playlistRequestDTO.getUserId());
                    });

            PlaylistEntity playlist = new PlaylistEntity();
            playlist.setNombre(playlistRequestDTO.getName());
            playlist.setDescripcion(playlistRequestDTO.getDescription());
            playlist.setIsPublic(playlistRequestDTO.getIsPublic());
            playlist.setImgPortada(playlistRequestDTO.getCoverImage());
            playlist.setUsuario(usuario);
            playlist.setFechaCreacion(LocalDate.now());

            playlist = playlistRepository.save(playlist);
            log.info("Playlist creada exitosamente: {} (ID: {})", playlist.getNombre(), playlist.getIdPlaylist());
        } catch (UserNotFoundException e) {
            throw e;
        } catch (DataAccessException e) {
            log.error("Error de acceso a datos al crear playlist: {}", e.getMessage());
            throw new PlaylistCreationException("Error al crear la playlist en la base de datos", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("Error inesperado al crear playlist: {}", e.getMessage());
            throw new PlaylistCreationException("Error inesperado al crear la playlist", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public void updatePlaylist(Long id, PlaylistRequestDTO playlistRequestDTO) {
        try {
            log.info("Iniciando actualización de playlist ID: {}", id);

            PlaylistEntity playlist = playlistRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("No se encontró la playlist a actualizar con ID: {}", id);
                        return new PlaylistNotFoundException("No se encontró la playlist con ID: " + id);
                    });

            playlist.setNombre(playlistRequestDTO.getName());
            playlist.setDescripcion(playlistRequestDTO.getDescription());
            playlist.setIsPublic(playlistRequestDTO.getIsPublic());
            playlist.setImgPortada(playlistRequestDTO.getCoverImage());

            playlist = playlistRepository.save(playlist);
            log.info("Playlist actualizada exitosamente: {} (ID: {})", playlist.getNombre(), playlist.getIdPlaylist());
        } catch (PlaylistNotFoundException e) {
            throw e;
        } catch (DataAccessException e) {
            log.error("Error de acceso a datos al actualizar playlist {}: {}", id, e.getMessage());
            throw new PlaylistUpdateException("Error al actualizar la playlist en la base de datos", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("Error inesperado al actualizar playlist {}: {}", id, e.getMessage());
            throw new PlaylistUpdateException("Error inesperado al actualizar la playlist", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public void deletePlaylist(Long id) {
        try {
            log.info("Iniciando eliminación de playlist ID: {}", id);

            if (!playlistRepository.existsById(id)) {
                log.error("No se encontró la playlist a eliminar con ID: {}", id);
                throw new PlaylistNotFoundException("No se encontró la playlist con ID: " + id);
            }

            playlistCancionRepository.findAll().stream()
                    .filter(pc -> pc.getPlaylist().getIdPlaylist().equals(id))
                    .forEach(pc -> {
                        playlistCancionRepository.delete(pc);
                        log.debug("Eliminada relación playlist-canción: Playlist {} - Canción {}",
                                pc.getPlaylist().getIdPlaylist(), pc.getCancion().getIdCancion());
                    });

            playlistRepository.deleteById(id);
            log.info("Playlist eliminada exitosamente (ID: {})", id);
        } catch (PlaylistNotFoundException e) {
            throw e;
        } catch (DataAccessException e) {
            log.error("Error de acceso a datos al eliminar playlist {}: {}", id, e.getMessage());
            throw new PlaylistDeletionException("Error al eliminar la playlist de la base de datos", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("Error inesperado al eliminar playlist {}: {}", id, e.getMessage());
            throw new PlaylistDeletionException("Error inesperado al eliminar la playlist", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public void addSongToPlaylist(Long playlistId, Long cancionId) {
        try {
            log.info("Iniciando proceso de agregar canción {} a playlist {}", cancionId, playlistId);

            PlaylistEntity playlist = playlistRepository.findById(playlistId)
                    .orElseThrow(() -> {
                        log.error("No se encontró la playlist con ID: {}", playlistId);
                        return new PlaylistNotFoundException("No se encontró la playlist con ID: " + playlistId);
                    });

            CancionEntity cancion = cancionRepository.findById(cancionId)
                    .orElseThrow(() -> {
                        log.error("No se encontró la canción con ID: {}", cancionId);
                        return new PlaylistNotFoundException("No se encontró la canción con ID: " + cancionId);
                    });

            boolean exists = playlistCancionRepository.findAll().stream()
                    .anyMatch(pc -> pc.getPlaylist().getIdPlaylist().equals(playlistId)
                            && pc.getCancion().getIdCancion().equals(cancionId));

            if (exists) {
                log.warn("La canción {} ya existe en la playlist {}", cancionId, playlistId);
                throw new PlaylistSongException("La canción ya existe en la playlist", HttpStatus.CONFLICT);
            }

            PlaylistCancionEntity playlistCancion = new PlaylistCancionEntity();
            playlistCancion.setPlaylist(playlist);
            playlistCancion.setCancion(cancion);

            playlistCancionRepository.save(playlistCancion);
            log.info("Canción {} agregada exitosamente a playlist {}", cancion.getTitulo(), playlist.getNombre());
        } catch (PlaylistNotFoundException | PlaylistSongException e) {
            throw e;
        } catch (DataAccessException e) {
            log.error("Error de acceso a datos al agregar canción {} a playlist {}: {}", cancionId, playlistId, e.getMessage());
            throw new PlaylistSongException("Error al agregar la canción a la playlist", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("Error inesperado al agregar canción {} a playlist {}: {}", cancionId, playlistId, e.getMessage());
            throw new PlaylistSongException("Error inesperado al agregar la canción", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public void removeSongFromPlaylist(Long playlistId, Long cancionId) {
        try {
            log.info("Iniciando proceso de remover canción {} de playlist {}", cancionId, playlistId);

            PlaylistEntity playlist = playlistRepository.findById(playlistId)
                    .orElseThrow(() -> {
                        log.error("No se encontró la playlist con ID: {}", playlistId);
                        return new PlaylistNotFoundException("No se encontró la playlist con ID: " + playlistId);
                    });

            CancionEntity cancion = cancionRepository.findById(cancionId)
                    .orElseThrow(() -> {
                        log.error("No se encontró la canción con ID: {}", cancionId);
                        return new PlaylistNotFoundException("No se encontró la canción con ID: " + cancionId);
                    });

            playlistCancionRepository.findAll().stream()
                    .filter(pc -> pc.getPlaylist().getIdPlaylist().equals(playlistId)
                            && pc.getCancion().getIdCancion().equals(cancionId))
                    .findFirst()
                    .ifPresentOrElse(
                            pc -> {
                                playlistCancionRepository.delete(pc);
                                log.info("Canción {} removida exitosamente de playlist {}",
                                        cancion.getTitulo(), playlist.getNombre());
                            },
                            () -> {
                                log.warn("No se encontró la canción {} en la playlist {}",
                                        cancion.getTitulo(), playlist.getNombre());
                                throw new PlaylistSongException("La canción no existe en la playlist", HttpStatus.NOT_FOUND);
                            }
                    );
        } catch (PlaylistNotFoundException | PlaylistSongException e) {
            throw e;
        } catch (DataAccessException e) {
            log.error("Error de acceso a datos al remover canción {} de playlist {}: {}",
                    cancionId, playlistId, e.getMessage());
            throw new PlaylistSongException("Error al remover la canción de la playlist", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("Error inesperado al remover canción {} de playlist {}: {}",
                    cancionId, playlistId, e.getMessage());
            throw new PlaylistSongException("Error inesperado al remover la canción", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private PlaylistResponseDTO convertToDTO(PlaylistEntity playlist) {
        PlaylistResponseDTO dto = new PlaylistResponseDTO();
        dto.setPlaylistId(playlist.getIdPlaylist());
        dto.setName(playlist.getNombre());
        dto.setDescription(playlist.getDescripcion());
        dto.setIsPublic(playlist.getIsPublic());
        dto.setCoverImage(playlist.getImgPortada());
        dto.setCreationDate(playlist.getFechaCreacion());
        dto.setUserId(playlist.getUsuario().getIdUsuario());
        return dto;
    }
}