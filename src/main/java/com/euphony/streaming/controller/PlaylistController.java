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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/playlists")
@Tag(name = "Gestión de Playlists", description = "API para la gestión de playlists")
@Slf4j
@RequiredArgsConstructor
public class PlaylistController {

    private final IPlaylistService playlistService;

    @Operation(summary = "Obtener todas las playlists", description = "Recupera una lista de todas las playlists")
    @ApiResponse(responseCode = "200", description = "Lista de playlists recuperada exitosamente",
            content = @Content(schema = @Schema(implementation = PlaylistResponseDTO.class)))
    @GetMapping("/all")
    public ResponseEntity<List<PlaylistResponseDTO>> getAllPlaylists() {
        return ResponseEntity.ok(playlistService.findAllPlaylists());
    }

    @Operation(summary = "Obtener playlist por ID", description = "Recupera una playlist específica por su ID")
    @ApiResponse(responseCode = "200", description = "Playlist encontrada",
            content = @Content(schema = @Schema(implementation = PlaylistResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Playlist no encontrada")
    @GetMapping("/search/{id}")
    public ResponseEntity<PlaylistResponseDTO> getPlaylistById(
            @Parameter(description = "ID de la playlist", required = true)
            @PathVariable Long id) {
        try {
            return ResponseEntity.ok(playlistService.findPlaylistById(id));
        } catch (PlaylistException e) {
            log.error("Error al buscar la playlist: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Crear una nueva playlist", description = "Crea una nueva playlist en el sistema")
    @ApiResponse(responseCode = "201", description = "Playlist creada exitosamente",
            content = @Content(schema = @Schema(implementation = PlaylistResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @PostMapping("/create")
    public ResponseEntity<PlaylistResponseDTO> createPlaylist(
            @Parameter(description = "Datos de la playlist a crear", required = true)
            @Valid @RequestBody PlaylistRequestDTO playlistRequestDTO) {
        try {
            PlaylistResponseDTO createdPlaylist = playlistService.createPlaylist(playlistRequestDTO);
            return ResponseEntity.created(URI.create("/api/v1/playlists/" + createdPlaylist.getIdPlaylist()))
                    .body(createdPlaylist);
        } catch (PlaylistException | UserNotFoundException e) {
            log.error("Error al crear la playlist: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar una playlist", description = "Actualiza los datos de una playlist existente")
    @ApiResponse(responseCode = "204", description = "Playlist actualizada exitosamente")
    @ApiResponse(responseCode = "404", description = "Playlist no encontrada")
    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updatePlaylist(
            @Parameter(description = "ID de la playlist a actualizar", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nuevos datos de la playlist", required = true)
            @Valid @RequestBody PlaylistRequestDTO playlistRequestDTO) {
        try {
            playlistService.updatePlaylist(id, playlistRequestDTO);
            return ResponseEntity.noContent().build();
        } catch (PlaylistException e) {
            log.error("Error al actualizar la playlist: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar una playlist", description = "Elimina una playlist existente del sistema")
    @ApiResponse(responseCode = "204", description = "Playlist eliminada exitosamente")
    @ApiResponse(responseCode = "404", description = "Playlist no encontrada")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePlaylist(
            @Parameter(description = "ID de la playlist a eliminar", required = true)
            @PathVariable Long id) {
        try {
            playlistService.deletePlaylist(id);
            return ResponseEntity.noContent().build();
        } catch (PlaylistException e) {
            log.error("Error al eliminar la playlist: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Añadir canción a playlist", description = "Agrega una canción a una playlist existente")
    @ApiResponse(responseCode = "200", description = "Canción añadida exitosamente",
            content = @Content(schema = @Schema(implementation = PlaylistResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Playlist o canción no encontrada")
    @PostMapping("/{playlistId}/songs/add/{songId}")
    public ResponseEntity<PlaylistResponseDTO> addSongToPlaylist(
            @Parameter(description = "ID de la playlist", required = true) @PathVariable Long playlistId,
            @Parameter(description = "ID de la canción", required = true) @PathVariable("songId") Long cancionId) {
        try {
            return ResponseEntity.ok(playlistService.addSongToPlaylist(playlistId, cancionId));
        } catch (PlaylistException e) {
            log.error("Error al añadir canción a la playlist: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Remover canción de playlist", description = "Elimina una canción de una playlist existente")
    @ApiResponse(responseCode = "200", description = "Canción removida exitosamente",
            content = @Content(schema = @Schema(implementation = PlaylistResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Playlist o canción no encontrada")
    @DeleteMapping("/{playlistId}/songs/remove/{songId}")
    public ResponseEntity<PlaylistResponseDTO> removeSongFromPlaylist(
            @Parameter(description = "ID de la playlist", required = true) @PathVariable Long playlistId,
            @Parameter(description = "ID de la canción", required = true) @PathVariable("songId") Long cancionId) {
        try {
            return ResponseEntity.ok(playlistService.removeSongFromPlaylist(playlistId, cancionId));
        } catch (PlaylistException e) {
            log.error("Error al remover canción de la playlist: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}