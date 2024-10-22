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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de playlists.
 * Proporciona la lógica de negocio para operaciones relacionadas con playlists.
 */
@Service
@RequiredArgsConstructor
public class PlaylistServiceImpl implements IPlaylistService {

    private final PlaylistRepository playlistRepository;
    private final UsuarioRepository usuarioRepository;
    private final CancionRepository cancionRepository;

    /**
     * Recupera todas las playlists.
     * @return Lista de PlaylistResponseDTO
     */
    @Override
    @Operation(summary = "Obtener todas las playlists", description = "Recupera una lista de todas las playlists disponibles")
    @ApiResponse(responseCode = "200", description = "Playlists recuperadas exitosamente",
            content = @Content(schema = @Schema(implementation = PlaylistResponseDTO.class)))
    public List<PlaylistResponseDTO> findAllPlaylists() {
        return playlistRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca una playlist por su ID.
     * @param id ID de la playlist
     * @return PlaylistResponseDTO
     * @throws PlaylistException si la playlist no se encuentra
     */
    @Override
    @Operation(summary = "Buscar playlist por ID", description = "Busca y devuelve una playlist específica por su ID")
    @ApiResponse(responseCode = "200", description = "Playlist encontrada",
            content = @Content(schema = @Schema(implementation = PlaylistResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Playlist no encontrada")
    public PlaylistResponseDTO findPlaylistById(Long id) {
        return playlistRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new PlaylistException("Playlist no encontrada con ID: " + id));
    }

    /**
     * Crea una nueva playlist.
     * @param playlistRequestDTO DTO con los datos de la nueva playlist
     * @return PlaylistResponseDTO de la playlist creada
     * @throws PlaylistException si hay un error al crear la playlist
     * @throws UserNotFoundException si el usuario no se encuentra
     */
    @Override
    @Transactional
    @Operation(summary = "Crear nueva playlist", description = "Crea una nueva playlist con los datos proporcionados")
    @ApiResponse(responseCode = "201", description = "Playlist creada exitosamente",
            content = @Content(schema = @Schema(implementation = PlaylistResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Datos de playlist inválidos")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
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

    /**
     * Actualiza una playlist existente.
     * @param id ID de la playlist a actualizar
     * @param playlistRequestDTO DTO con los nuevos datos de la playlist
     * @return PlaylistResponseDTO de la playlist actualizada
     * @throws PlaylistException si la playlist no se encuentra o hay un error al actualizarla
     */
    @Override
    @Transactional
    @Operation(summary = "Actualizar playlist", description = "Actualiza una playlist existente con los datos proporcionados")
    @ApiResponse(responseCode = "200", description = "Playlist actualizada exitosamente",
            content = @Content(schema = @Schema(implementation = PlaylistResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Playlist no encontrada")
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

    /**
     * Elimina una playlist.
     * @param id ID de la playlist a eliminar
     * @throws PlaylistException si la playlist no se encuentra
     */
    @Override
    @Transactional
    @Operation(summary = "Eliminar playlist", description = "Elimina una playlist existente por su ID")
    @ApiResponse(responseCode = "204", description = "Playlist eliminada exitosamente")
    @ApiResponse(responseCode = "404", description = "Playlist no encontrada")
    public void deletePlaylist(Long id) {
        if (!playlistRepository.existsById(id)) {
            throw new PlaylistException("Playlist no encontrada con ID: " + id);
        }
        playlistRepository.deleteById(id);
    }

    /**
     * Añade una canción a una playlist.
     * @param playlistId ID de la playlist
     * @param cancionId ID de la canción a añadir
     * @return PlaylistResponseDTO actualizada
     * @throws PlaylistException si la playlist o la canción no se encuentran
     */
    @Override
    @Transactional
    @Operation(summary = "Añadir canción a playlist", description = "Añade una canción existente a una playlist")
    @ApiResponse(responseCode = "200", description = "Canción añadida exitosamente",
            content = @Content(schema = @Schema(implementation = PlaylistResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Playlist o canción no encontrada")
    public PlaylistResponseDTO addSongToPlaylist(Long playlistId, Long cancionId) {
        PlaylistEntity playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistException("Playlist no encontrada con ID: " + playlistId));
        CancionEntity cancion = cancionRepository.findById(cancionId)
                .orElseThrow(() -> new PlaylistException("Canción no encontrada con ID: " + cancionId));

        // Aquí iría la lógica para añadir la canción a la playlist
        // playlist.getCanciones().add(cancion);

        PlaylistEntity updatedPlaylist = playlistRepository.save(playlist);
        return convertToDTO(updatedPlaylist);
    }

    /**
     * Remueve una canción de una playlist.
     * @param playlistId ID de la playlist
     * @param cancionId ID de la canción a remover
     * @return PlaylistResponseDTO actualizada
     * @throws PlaylistException si la playlist o la canción no se encuentran
     */
    @Override
    @Transactional
    @Operation(summary = "Remover canción de playlist", description = "Remueve una canción de una playlist existente")
    @ApiResponse(responseCode = "200", description = "Canción removida exitosamente",
            content = @Content(schema = @Schema(implementation = PlaylistResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Playlist o canción no encontrada")
    public PlaylistResponseDTO removeSongFromPlaylist(Long playlistId, Long cancionId) {
        PlaylistEntity playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistException("Playlist no encontrada con ID: " + playlistId));
        CancionEntity cancion = cancionRepository.findById(cancionId)
                .orElseThrow(() -> new PlaylistException("Canción no encontrada con ID: " + cancionId));

        // Aquí iría la lógica para remover la canción de la playlist
        // playlist.getCanciones().remove(cancion);

        PlaylistEntity updatedPlaylist = playlistRepository.save(playlist);
        return convertToDTO(updatedPlaylist);
    }

    /**
     * Convierte una entidad PlaylistEntity a PlaylistResponseDTO.
     * @param playlist Entidad PlaylistEntity a convertir
     * @return PlaylistResponseDTO convertido
     */
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