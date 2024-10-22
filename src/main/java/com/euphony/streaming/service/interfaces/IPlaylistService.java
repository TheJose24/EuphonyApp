package com.euphony.streaming.service.interfaces;

import com.euphony.streaming.dto.request.PlaylistRequestDTO;
import com.euphony.streaming.dto.response.PlaylistResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

/**
 * Interfaz que define las operaciones del servicio de playlists.
 * Proporciona métodos para la gestión completa de playlists.
 */
public interface IPlaylistService {

    /**
     * Recupera todas las playlists.
     * @return Lista de PlaylistResponseDTO
     */
    @Operation(summary = "Obtener todas las playlists")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de playlists obtenida con éxito"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    List<PlaylistResponseDTO> findAllPlaylists();

    /**
     * Busca una playlist por su ID.
     * @param id ID de la playlist a buscar
     * @return PlaylistResponseDTO de la playlist encontrada
     */
    @Operation(summary = "Buscar playlist por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Playlist encontrada"),
            @ApiResponse(responseCode = "404", description = "Playlist no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    PlaylistResponseDTO findPlaylistById(@Parameter(description = "ID de la playlist") Long id);

    /**
     * Crea una nueva playlist.
     * @param playlistRequestDTO DTO con los datos de la nueva playlist
     * @return PlaylistResponseDTO de la playlist creada
     */
    @Operation(summary = "Crear nueva playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Playlist creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos para crear la playlist"),
            @ApiResponse(responseCode = "403", description = "No autorizado para crear playlist"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    PlaylistResponseDTO createPlaylist(@Parameter(description = "Datos de la nueva playlist") PlaylistRequestDTO playlistRequestDTO);

    /**
     * Actualiza una playlist existente.
     * @param id ID de la playlist a actualizar
     * @param playlistRequestDTO DTO con los nuevos datos de la playlist
     * @return PlaylistResponseDTO de la playlist actualizada
     */
    @Operation(summary = "Actualizar playlist existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Playlist actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos para actualizar la playlist"),
            @ApiResponse(responseCode = "403", description = "No autorizado para actualizar esta playlist"),
            @ApiResponse(responseCode = "404", description = "Playlist no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    PlaylistResponseDTO updatePlaylist(@Parameter(description = "ID de la playlist a actualizar") Long id,
                                       @Parameter(description = "Nuevos datos de la playlist") PlaylistRequestDTO playlistRequestDTO);

    /**
     * Elimina una playlist.
     * @param id ID de la playlist a eliminar
     */
    @Operation(summary = "Eliminar playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Playlist eliminada exitosamente"),
            @ApiResponse(responseCode = "403", description = "No autorizado para eliminar esta playlist"),
            @ApiResponse(responseCode = "404", description = "Playlist no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    void deletePlaylist(@Parameter(description = "ID de la playlist a eliminar") Long id);

    /**
     * Añade una canción a una playlist.
     * @param playlistId ID de la playlist
     * @param cancionId ID de la canción a añadir
     * @return PlaylistResponseDTO actualizada
     */
    @Operation(summary = "Añadir canción a la playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Canción añadida exitosamente"),
            @ApiResponse(responseCode = "403", description = "No autorizado para modificar esta playlist"),
            @ApiResponse(responseCode = "404", description = "Playlist o canción no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    PlaylistResponseDTO addSongToPlaylist(@Parameter(description = "ID de la playlist") Long playlistId,
                                          @Parameter(description = "ID de la canción a añadir") Long cancionId);

    /**
     * Remueve una canción de una playlist.
     * @param playlistId ID de la playlist
     * @param cancionId ID de la canción a remover
     * @return PlaylistResponseDTO actualizada
     */
    @Operation(summary = "Remover canción de la playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Canción removida exitosamente"),
            @ApiResponse(responseCode = "403", description = "No autorizado para modificar esta playlist"),
            @ApiResponse(responseCode = "404", description = "Playlist o canción no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    PlaylistResponseDTO removeSongFromPlaylist(@Parameter(description = "ID de la playlist") Long playlistId,
                                               @Parameter(description = "ID de la canción a remover") Long cancionId);
}