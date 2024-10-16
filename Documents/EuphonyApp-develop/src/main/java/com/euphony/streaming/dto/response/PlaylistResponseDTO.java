package com.euphony.streaming.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO for returning playlist information")
public class PlaylistResponseDTO {

    @Schema(description = "ID único de la playlist", example = "123e4567-e89b-12d3-a456-426614174000")
    private String idPlaylist;

    @Schema(description = "Nombre de la playlist", example = "Mi Playlist Favorita")
    private String nombre;

    @Schema(description = "Descripción de la playlist", example = "Una colección de mis canciones favoritas")
    private String descripcion;

    @Schema(description = "Indica si la playlist es pública", example = "true")
    @JsonProperty("isPublic")
    private Boolean isPublic;

    @Schema(description = "URL de la imagen de portada de la playlist", example = "https://ejemplo.com/imagen.jpg")
    private String imgPortada;

    @Schema(description = "Fecha de creación de la playlist")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaCreacion;

    @Schema(description = "ID del usuario propietario de la playlist", example = "9f8cdedc-56cc-40eb-a06b-3afc3e59ac80")
    private String idUsuario;

    @Schema(description = "Lista de IDs de canciones en la playlist", example = "[\"123e4567-e89b-12d3-a456-426614174001\", \"123e4567-e89b-12d3-a456-426614174002\"]")
    private List<String> idsCanciones;

    @Schema(description = "Número total de canciones en la playlist", example = "15")
    private int numeroCanciones;

    @Schema(description = "Duración total de la playlist en segundos", example = "3600")
    private long duracionTotal;
}