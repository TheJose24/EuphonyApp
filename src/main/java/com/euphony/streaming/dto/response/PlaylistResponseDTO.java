package com.euphony.streaming.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta para la información de playlists.")
public class PlaylistResponseDTO {

    @Schema(description = "Identificador único de la playlist", example = "1")
    @NotNull(message = "El ID de la playlist no puede ser null")
    private Long idPlaylist;

    @Schema(description = "Nombre de la playlist", example = "Mi Playlist de Rock")
    @NotBlank(message = "El nombre de la playlist no puede estar vacío")
    @Size(min = 3, max = 255, message = "El nombre debe tener entre 3 y 255 caracteres")
    private String nombre;

    @Schema(description = "Fecha de creación de la playlist", example = "2024-01-01")
    @NotNull(message = "La fecha de creación no puede ser null")
    @PastOrPresent(message = "La fecha de creación no puede ser futura")
    private LocalDate fechaCreacion;

    @Schema(description = "Descripción de la playlist", example = "Colección de mis canciones favoritas de rock")
    @Size(max = 1000, message = "La descripción no puede exceder los 1000 caracteres")
    private String descripcion;

    @Schema(description = "Indica si la playlist es pública", example = "false", defaultValue = "false")
    @NotNull(message = "El indicador de visibilidad no puede ser null")
    @Builder.Default
    private Boolean isPublic = false;

    @Schema(description = "URL de la imagen de portada", example = "https://ejemplo.com/imagen.jpg")
    @Size(max = 255, message = "La URL de la imagen no puede exceder los 255 caracteres")
    @Pattern(regexp = "^(https?://.*|)$", message = "La URL debe comenzar con http:// o https:// o estar vacía")
    private String imgPortada;

    @Schema(description = "ID del usuario propietario de la playlist", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "El ID del usuario no puede ser null")
    private UUID idUsuario;
}