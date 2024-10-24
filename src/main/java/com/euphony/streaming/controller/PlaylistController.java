package com.euphony.streaming.controller;

import com.euphony.streaming.dto.request.PlaylistRequestDTO;
import com.euphony.streaming.dto.response.PlaylistResponseDTO;
import com.euphony.streaming.exception.custom.*;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/playlists")
@Tag(name = "Gestión de Playlists", description = "API para la gestión de listas de reproducción")
@Slf4j
@RequiredArgsConstructor
public class PlaylistController {

    private final IPlaylistService playlistService;

    @Operation(summary = "Obtener todas las playlists",
            description = "Recupera una lista de todas las listas de reproducción disponibles")
    @ApiResponse(responseCode = "200", description = "Playlists recuperadas exitosamente",
            content = @Content(schema = @Schema(implementation = PlaylistResponseDTO.class)))
    @GetMapping
    public ResponseEntity<List<PlaylistResponseDTO>> getAllPlaylists() {
        log.info("Solicitando todas las playlists");
        List<PlaylistResponseDTO> playlists = playlistService.findAllPlaylists();
        log.info("Se encontraron {} playlists", playlists.size());
        return ResponseEntity.ok(playlists);
    }

    @Operation(summary = "Obtener playlist por ID",
            description = "Recupera una lista de reproducción específica por su ID")
    @ApiResponse(responseCode = "200", description = "Playlist encontrada",
            content = @Content(schema = @Schema(implementation = PlaylistResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Playlist no encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<PlaylistResponseDTO> getPlaylistById(
            @Parameter(description = "ID de la playlist", required = true)
            @PathVariable Long id) {
        log.info("Buscando playlist con ID: {}", id);
        PlaylistResponseDTO playlist = playlistService.findPlaylistById(id);
        log.info("Playlist encontrada con ID: {}", id);
        return ResponseEntity.ok(playlist);
    }

    @Operation(summary = "Crear nueva playlist",
            description = "Crea una nueva lista de reproducción en el sistema")
    @ApiResponse(responseCode = "201", description = "Playlist creada exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @PostMapping
    public ResponseEntity<Void> createPlaylist(
            @Parameter(description = "Datos de la playlist a crear", required = true)
            @Valid @RequestBody PlaylistRequestDTO playlistRequestDTO) {
        log.info("Creando nueva playlist para usuario ID: {}", playlistRequestDTO.getUserId());
        playlistService.createPlaylist(playlistRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Actualizar playlist",
            description = "Actualiza los datos de una lista de reproducción existente")
    @ApiResponse(responseCode = "204", description = "Playlist actualizada exitosamente")
    @ApiResponse(responseCode = "404", description = "Playlist no encontrada")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePlaylist(
            @Parameter(description = "ID de la playlist a actualizar", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nuevos datos de la playlist", required = true)
            @Valid @RequestBody PlaylistRequestDTO playlistRequestDTO) {
        log.info("Actualizando playlist ID: {}", id);
        playlistService.updatePlaylist(id, playlistRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Eliminar playlist",
            description = "Elimina una lista de reproducción existente del sistema")
    @ApiResponse(responseCode = "204", description = "Playlist eliminada exitosamente")
    @ApiResponse(responseCode = "404", description = "Playlist no encontrada")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlaylist(
            @Parameter(description = "ID de la playlist a eliminar", required = true)
            @PathVariable Long id) {
        log.info("Eliminando playlist ID: {}", id);
        playlistService.deletePlaylist(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Agregar canción a playlist",
            description = "Agrega una canción a una lista de reproducción existente")
    @ApiResponse(responseCode = "204", description = "Canción agregada exitosamente")
    @ApiResponse(responseCode = "404", description = "Playlist o canción no encontrada")
    @ApiResponse(responseCode = "409", description = "La canción ya existe en la playlist")
    @PostMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<Void> addSongToPlaylist(
            @Parameter(description = "ID de la playlist", required = true) @PathVariable Long playlistId,
            @Parameter(description = "ID de la canción", required = true) @PathVariable Long songId) {
        log.info("Agregando canción {} a playlist {}", songId, playlistId);
        playlistService.addSongToPlaylist(playlistId, songId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Eliminar canción de playlist",
            description = "Elimina una canción de una lista de reproducción existente")
    @ApiResponse(responseCode = "204", description = "Canción eliminada exitosamente")
    @ApiResponse(responseCode = "404", description = "Playlist o canción no encontrada")
    @DeleteMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<Void> removeSongFromPlaylist(
            @Parameter(description = "ID de la playlist", required = true) @PathVariable Long playlistId,
            @Parameter(description = "ID de la canción", required = true) @PathVariable Long songId) {
        log.info("Eliminando canción {} de playlist {}", songId, playlistId);
        playlistService.removeSongFromPlaylist(playlistId, songId);
        return ResponseEntity.noContent().build();
    }
}