/**
 * Controlador REST para la gestión de playlists.
 * Esta clase maneja las operaciones CRUD para las playlists en la aplicación.
 */
package com.euphony.streaming.controller;

import com.euphony.streaming.dto.request.PlaylistRequestDTO;
import com.euphony.streaming.dto.response.PlaylistResponseDTO;
import com.euphony.streaming.service.interfaces.PlaylistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/playlists")
@RequiredArgsConstructor
@Tag(name = "Playlist", description = "API para gestionar playlists")
public class PlaylistController {

    private final PlaylistService playlistService;

    /**
     * Crea una nueva playlist.
     *
     * @param playlistRequestDTO DTO con los datos de la playlist a crear
     * @return ResponseEntity con la playlist creada y código de estado 201 (CREATED)
     */
    @PostMapping
    @Operation(summary = "Crear una nueva playlist")
    @ApiResponse(responseCode = "201", description = "Playlist creada exitosamente")
    @ApiResponse(responseCode = "401", description = "No autorizado")
    @ApiResponse(responseCode = "409", description = "Conflicto al crear la playlist")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    public ResponseEntity<PlaylistResponseDTO> createPlaylist(@Valid @RequestBody PlaylistRequestDTO playlistRequestDTO) {
        PlaylistResponseDTO createdPlaylist = playlistService.createPlaylist(playlistRequestDTO);
        return new ResponseEntity<>(createdPlaylist, HttpStatus.CREATED);
    }

    /**
     * Obtiene todas las playlists.
     *
     * @return ResponseEntity con la lista de todas las playlists y código de estado 200 (OK)
     */
    @GetMapping
    @Operation(summary = "Obtener todas las playlists")
    @ApiResponse(responseCode = "200", description = "Lista de playlists")
    @ApiResponse(responseCode = "401", description = "No autorizado")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    public ResponseEntity<List<PlaylistResponseDTO>> getAllPlaylists() {
        List<PlaylistResponseDTO> playlists = playlistService.getAllPlaylists();
        return ResponseEntity.ok(playlists);
    }

    /**
     * Obtiene una playlist por su ID.
     *
     * @param id ID de la playlist a buscar
     * @return ResponseEntity con la playlist encontrada y código de estado 200 (OK)
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener una playlist por ID")
    @ApiResponse(responseCode = "200", description = "Playlist encontrada")
    @ApiResponse(responseCode = "401", description = "No autorizado")
    @ApiResponse(responseCode = "404", description = "Playlist no encontrada")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    public ResponseEntity<PlaylistResponseDTO> getPlaylistById(@PathVariable Long id) {
        PlaylistResponseDTO playlist = playlistService.getPlaylistById(id);
        return ResponseEntity.ok(playlist);
    }

    /**
     * Actualiza una playlist existente.
     *
     * @param id ID de la playlist a actualizar
     * @param playlistRequestDTO DTO con los nuevos datos de la playlist
     * @return ResponseEntity con la playlist actualizada y código de estado 200 (OK)
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una playlist existente")
    @ApiResponse(responseCode = "200", description = "Playlist actualizada exitosamente")
    @ApiResponse(responseCode = "401", description = "No autorizado")
    @ApiResponse(responseCode = "404", description = "Playlist no encontrada")
    @ApiResponse(responseCode = "409", description = "Conflicto al actualizar la playlist")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    public ResponseEntity<PlaylistResponseDTO> updatePlaylist(@PathVariable Long id, @Valid @RequestBody PlaylistRequestDTO playlistRequestDTO) {
        PlaylistResponseDTO updatedPlaylist = playlistService.updatePlaylist(id, playlistRequestDTO);
        return ResponseEntity.ok(updatedPlaylist);
    }

    /**
     * Elimina una playlist por su ID.
     *
     * @param id ID de la playlist a eliminar
     * @return ResponseEntity sin contenido y código de estado 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una playlist")
    @ApiResponse(responseCode = "204", description = "Playlist eliminada exitosamente")
    @ApiResponse(responseCode = "401", description = "No autorizado")
    @ApiResponse(responseCode = "404", description = "Playlist no encontrada")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    public ResponseEntity<Void> deletePlaylist(@PathVariable Long id) {
        playlistService.deletePlaylist(id);
        return ResponseEntity.noContent().build();
    }
}