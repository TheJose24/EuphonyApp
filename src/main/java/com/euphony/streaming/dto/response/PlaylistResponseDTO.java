package com.euphony.streaming.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO para las respuestas que contienen información de playlists.
 * Se utiliza para enviar datos de playlist al cliente.
 */
@Data
@Schema(description = "Representación de una playlist en las respuestas de la API")
public class PlaylistResponseDTO {

    @Schema(description = "Identificador único de la playlist", example = "1")
    private Long idPlaylist;

    @Schema(description = "Nombre de la playlist", example = "Mi Playlist Favorita")
    private String nombre;

    @Schema(description = "Fecha de creación de la playlist", example = "2023-01-01")
    private LocalDate fechaCreacion;

    @Schema(description = "Descripción de la playlist", example = "Una colección de mis canciones favoritas")
    private String descripcion;

    @Schema(description = "Indica si la playlist es pública", example = "true")
    private Boolean isPublic;

    @Schema(description = "URL de la imagen de portada de la playlist", example = "http://ejemplo.com/imagen.jpg")
    private String imgPortada;

    @Schema(description = "Identificador único del usuario propietario de la playlist", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID idUsuario;
}