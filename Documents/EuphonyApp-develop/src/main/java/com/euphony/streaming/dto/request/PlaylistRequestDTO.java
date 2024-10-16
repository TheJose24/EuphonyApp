package com.euphony.streaming.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO for creating or updating a playlist")
public class PlaylistRequestDTO {

    @NotBlank(message = "El nombre de la playlist no puede estar vacío")
    @Size(min = 1, max = 255, message = "El nombre de la playlist debe tener entre 1 y 255 caracteres")
    @Schema(description = "Nombre de la playlist", example = "Mi Playlist Favorita")
    private String nombre;

    @Size(max = 1000, message = "La descripción no puede exceder los 1000 caracteres")
    @Schema(description = "Descripción de la playlist", example = "Una colección de mis canciones favoritas")
    private String descripcion;

    @NotNull(message = "Debe especificar si la playlist es pública o privada")
    @Schema(description = "Indica si la playlist es pública", example = "true")
    @JsonProperty("isPublic")
    private Boolean isPublic;

    @Size(max = 255, message = "La URL de la imagen de portada no puede exceder los 255 caracteres")
    @Schema(description = "URL de la imagen de portada de la playlist", example = "https://ejemplo.com/imagen.jpg")
    private String imgPortada;

    @NotNull(message = "El ID del usuario no puede ser nulo")
    @Schema(description = "ID del usuario propietario de la playlist", example = "9f8cdedc-56cc-40eb-a06b-3afc3e59ac80")
    private String idUsuario; // Cambiado de Long a String
}