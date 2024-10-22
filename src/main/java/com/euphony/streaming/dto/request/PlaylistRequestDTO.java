package com.euphony.streaming.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

/**
 * DTO para las solicitudes de creación o actualización de playlists.
 * Contiene los datos necesarios para crear o modificar una playlist.
 */
@Data
public class PlaylistRequestDTO {

    /**
     * Nombre de la playlist. Requerido, máximo 255 caracteres.
     */
    @NotBlank(message = "El nombre de la playlist no puede estar vacío")
    @Size(max = 255, message = "El nombre de la playlist no puede exceder los 255 caracteres")
    private String nombre;

    /**
     * Descripción de la playlist. Opcional.
     */
    @Size(max = 1000, message = "La descripción no puede exceder los 1000 caracteres")
    private String descripcion;

    /**
     * Indica si la playlist es pública (true) o privada (false).
     */
    private Boolean isPublic;

    /**
     * URL de la imagen de portada de la playlist. Opcional.
     */
    @Size(max = 255, message = "La URL de la imagen no puede exceder los 255 caracteres")
    private String imgPortada;

    /**
     * ID del usuario propietario de la playlist. Requerido.
     */
    @NotNull(message = "El ID del usuario no puede ser null")
    private UUID idUsuario;
}