package com.euphony.streaming.service.implementation;

import com.euphony.streaming.dto.request.PlaylistRequestDTO;
import com.euphony.streaming.dto.response.PlaylistResponseDTO;
import com.euphony.streaming.entity.CancionEntity;
import com.euphony.streaming.entity.PlaylistEntity;
import com.euphony.streaming.entity.UsuarioEntity;
import com.euphony.streaming.exception.custom.PlaylistException;
import com.euphony.streaming.exception.custom.UserNotFoundException;
import com.euphony.streaming.repository.CancionRepository;
import com.euphony.streaming.repository.PlaylistRepository;
import com.euphony.streaming.repository.UsuarioRepository;
import com.euphony.streaming.service.interfaces.IPlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

// Servicio que implementa las operaciones de gestión de playlists
@Service
@RequiredArgsConstructor
public class PlaylistServiceImpl implements IPlaylistService {

    private final PlaylistRepository playlistRepository;
    private final UsuarioRepository usuarioRepository;
    private final CancionRepository cancionRepository;

    // Retorna lista de todas las playlists existentes
    @Override
    public List<PlaylistResponseDTO> findAllPlaylists() {
        return playlistRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Busca una playlist específica por su ID
    @Override
    public PlaylistResponseDTO findPlaylistById(Long id) {
        return playlistRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new PlaylistException("Playlist no encontrada con ID: " + id));
    }

    // Crea una nueva playlist con los datos proporcionados
    @Override
    @Transactional
    public PlaylistResponseDTO createPlaylist(PlaylistRequestDTO playlistRequestDTO) {
        try {
            UsuarioEntity usuario = usuarioRepository.findById(playlistRequestDTO.getIdUsuario())
                    .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID: " + playlistRequestDTO.getIdUsuario()));

            PlaylistEntity playlist = new PlaylistEntity();
            playlist.setNombre(playlistRequestDTO.getNombre());
            playlist.setDescripcion(playlistRequestDTO.getDescripcion());
            playlist.setIsPublic(playlistRequestDTO.getIsPublic());
            playlist.setImgPortada(playlistRequestDTO.getImgPortada());
            playlist.setUsuario(usuario);
            playlist.setFechaCreacion(LocalDate.now());

            PlaylistEntity savedPlaylist = playlistRepository.save(playlist);
            return convertToDTO(savedPlaylist);
        } catch (Exception e) {
            throw new PlaylistException("Error al crear la playlist", e);
        }
    }

    // Actualiza los datos de una playlist existente
    @Override
    @Transactional
    public PlaylistResponseDTO updatePlaylist(Long id, PlaylistRequestDTO playlistRequestDTO) {
        PlaylistEntity playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new PlaylistException("Playlist no encontrada con ID: " + id));

        playlist.setNombre(playlistRequestDTO.getNombre());
        playlist.setDescripcion(playlistRequestDTO.getDescripcion());
        playlist.setIsPublic(playlistRequestDTO.getIsPublic());
        playlist.setImgPortada(playlistRequestDTO.getImgPortada());

        PlaylistEntity updatedPlaylist = playlistRepository.save(playlist);
        return convertToDTO(updatedPlaylist);
    }

    // Elimina una playlist del sistema
    @Override
    @Transactional
    public void deletePlaylist(Long id) {
        if (!playlistRepository.existsById(id)) {
            throw new PlaylistException("Playlist no encontrada con ID: " + id);
        }
        playlistRepository.deleteById(id);
    }

    // Agrega una canción existente a una playlist
    @Override
    @Transactional
    public PlaylistResponseDTO addSongToPlaylist(Long playlistId, Long cancionId) {
        PlaylistEntity playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistException("Playlist no encontrada con ID: " + playlistId));
        CancionEntity cancion = cancionRepository.findById(cancionId)
                .orElseThrow(() -> new PlaylistException("Canción no encontrada con ID: " + cancionId));

        // TODO: Implementar lógica para añadir la canción a la playlist
        // playlist.getCanciones().add(cancion);

        PlaylistEntity updatedPlaylist = playlistRepository.save(playlist);
        return convertToDTO(updatedPlaylist);
    }

    // Elimina una canción de una playlist existente
    @Override
    @Transactional
    public PlaylistResponseDTO removeSongFromPlaylist(Long playlistId, Long cancionId) {
        PlaylistEntity playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistException("Playlist no encontrada con ID: " + playlistId));
        CancionEntity cancion = cancionRepository.findById(cancionId)
                .orElseThrow(() -> new PlaylistException("Canción no encontrada con ID: " + cancionId));

        // TODO: Implementar lógica para remover la canción de la playlist
        // playlist.getCanciones().remove(cancion);

        PlaylistEntity updatedPlaylist = playlistRepository.save(playlist);
        return convertToDTO(updatedPlaylist);
    }

    // Convierte una entidad PlaylistEntity a PlaylistResponseDTO
    private PlaylistResponseDTO convertToDTO(PlaylistEntity playlist) {
        PlaylistResponseDTO dto = new PlaylistResponseDTO();
        dto.setIdPlaylist(playlist.getIdPlaylist());
        dto.setNombre(playlist.getNombre());
        dto.setDescripcion(playlist.getDescripcion());
        dto.setIsPublic(playlist.getIsPublic());
        dto.setImgPortada(playlist.getImgPortada());
        dto.setFechaCreacion(playlist.getFechaCreacion());
        dto.setIdUsuario(playlist.getUsuario().getIdUsuario());
        return dto;
    }
}