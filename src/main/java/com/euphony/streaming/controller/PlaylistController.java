package com.euphony.streaming.controller;

import com.euphony.streaming.dto.request.PlaylistRequestDTO;
import com.euphony.streaming.dto.response.PlaylistResponseDTO;
import com.euphony.streaming.exception.custom.PlaylistException;
import com.euphony.streaming.exception.custom.UserNotFoundException;
import com.euphony.streaming.service.interfaces.IPlaylistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Controlador para la gestión de playlists.
 * Proporciona endpoints para crear, leer, actualizar y eliminar playlists,
 * así como para añadir y remover canciones de las playlists.
 */
@RestController
@RequestMapping("/api/v1/playlists")
@Tag(name = "Gestión de Playlists", description = "API para la gestión completa de playlists")
@Slf4j
@RequiredArgsConstructor
public class PlaylistController {

    private final IPlaylistService playlistService;

    @Operation(summary = "Listar todas las playlists", description = "Obtiene una lista de todas las playlists disponibles en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de playlists obtenida con éxito",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlaylistResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<PlaylistResponseDTO>> getAllPlaylists() {
        List<PlaylistResponseDTO> playlists = playlistService.findAllPlaylists();
        return ResponseEntity.ok(playlists);
    }

    @Operation(summary = "Buscar playlist por ID", description = "Busca y devuelve una playlist específica basada en su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Playlist encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlaylistResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Playlist no encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PlaylistResponseDTO> getPlaylistById(
            @Parameter(description = "ID de la playlist", required = true)
            @PathVariable Long id) {
        try {
            PlaylistResponseDTO playlist = playlistService.findPlaylistById(id);
            return ResponseEntity.ok(playlist);
        } catch (PlaylistException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Crear nueva playlist", description = "Crea una nueva playlist con la información proporcionada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Playlist creada exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlaylistResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos para crear la playlist",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<PlaylistResponseDTO> createPlaylist(
            @Parameter(description = "Datos de la nueva playlist", required = true)
            @Valid @RequestBody PlaylistRequestDTO playlistRequestDTO) {
        try {
            PlaylistResponseDTO createdPlaylist = playlistService.createPlaylist(playlistRequestDTO);
            return ResponseEntity.created(URI.create("/api/v1/playlists/" + createdPlaylist.getIdPlaylist()))
                    .body(createdPlaylist);
        } catch (PlaylistException | UserNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar playlist existente", description = "Actualiza la información de una playlist existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Playlist actualizada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlaylistResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos para actualizar la playlist",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Playlist no encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<PlaylistResponseDTO> updatePlaylist(
            @Parameter(description = "ID de la playlist a actualizar", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nuevos datos de la playlist", required = true)
            @Valid @RequestBody PlaylistRequestDTO playlistRequestDTO) {
        try {
            PlaylistResponseDTO updatedPlaylist = playlistService.updatePlaylist(id, playlistRequestDTO);
            return ResponseEntity.ok(updatedPlaylist);
        } catch (PlaylistException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar playlist", description = "Elimina una playlist existente del sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Playlist eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Playlist no encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlaylist(
            @Parameter(description = "ID de la playlist a eliminar", required = true)
            @PathVariable Long id) {
        try {
            playlistService.deletePlaylist(id);
            return ResponseEntity.noContent().build();
        } catch (PlaylistException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Añadir canción a la playlist", description = "Agrega una canción específica a una playlist existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Canción añadida exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlaylistResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Playlist o canción no encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Error al añadir la canción",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @PostMapping("/{playlistId}/canciones/{cancionId}")
    public ResponseEntity<PlaylistResponseDTO> addSongToPlaylist(
            @Parameter(description = "ID de la playlist", required = true)
            @PathVariable Long playlistId,
            @Parameter(description = "ID de la canción a añadir", required = true)
            @PathVariable Long cancionId) {
        try {
            PlaylistResponseDTO updatedPlaylist = playlistService.addSongToPlaylist(playlistId, cancionId);
            return ResponseEntity.ok(updatedPlaylist);
        } catch (PlaylistException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Remover canción de la playlist", description = "Elimina una canción específica de una playlist existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Canción removida exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlaylistResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Playlist o canción no encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Error al remover la canción",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @DeleteMapping("/{playlistId}/canciones/{cancionId}")
    public ResponseEntity<PlaylistResponseDTO> removeSongFromPlaylist(
            @Parameter(description = "ID de la playlist", required = true)
            @PathVariable Long playlistId,
            @Parameter(description = "ID de la canción a remover", required = true)
            @PathVariable Long cancionId) {
        try {
            PlaylistResponseDTO updatedPlaylist = playlistService.removeSongFromPlaylist(playlistId, cancionId);
            return ResponseEntity.ok(updatedPlaylist);
        } catch (PlaylistException e) {
            return ResponseEntity.notFound().build();
        }
    }
}