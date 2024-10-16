package com.euphony.streaming.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserKeycloakResponseDTO {

    @Schema(description = "ID único del usuario en Keycloak", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Nombre de usuario único", example = "johndoe")
    private String username;

    @Schema(description = "Correo electrónico del usuario", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Nombre de pila del usuario", example = "John")
    private String firstName;

    @Schema(description = "Apellido del usuario", example = "Doe")
    private String lastName;

    @Schema(description = "Roles asignados al usuario", example = "[\"artist_client_role\", \"user_client_role\"]")
    private Set<String> roles;

    @Schema(description = "Estado del correo electrónico", example = "true")
    private boolean emailVerified;

    @Schema(description = "Estado del usuario", example = "true")
    private boolean enabled;
}
