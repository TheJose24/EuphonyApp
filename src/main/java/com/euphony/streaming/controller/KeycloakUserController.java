package com.euphony.streaming.controller;

import com.euphony.streaming.dto.request.UserKeycloakRequestDTO;
import com.euphony.streaming.dto.response.UserKeycloakResponseDTO;
import com.euphony.streaming.service.interfaces.IKeycloakService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.NotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/keycloak/usuarios")
@Tag(name = "Gestión de Usuarios Keycloak", description = "API para la gestión de usuarios en Keycloak")
@PreAuthorize("hasRole('admin_client_role')")
public class KeycloakUserController {

    private final IKeycloakService keycloakService;

    @Autowired
    public KeycloakUserController(IKeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    @Operation(summary = "Listar todos los usuarios",
            description = "Obtiene una lista completa de todos los usuarios registrados en Keycloak.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida con éxito",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserKeycloakResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "No autorizado para acceder a este recurso",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @GetMapping("/listar")
    public ResponseEntity<List<UserKeycloakResponseDTO>> findAllUsers() {
        List<UserKeycloakResponseDTO> usuarios = keycloakService.findAllUsers()
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
        return ResponseEntity.ok(usuarios);
    }

    @Operation(summary = "Buscar usuario por nombre de usuario",
            description = "Busca un usuario específico en Keycloak utilizando su nombre de usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserKeycloakResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @GetMapping("/listar/{username}")
    public ResponseEntity<UserKeycloakResponseDTO> findUser(
            @Parameter(description = "Nombre de usuario a buscar", required = true, example = "johndoe")
            @PathVariable String username) {
        UserRepresentation usuario = keycloakService.searchUserByUsername(username);
        return ResponseEntity.ok(convertToResponseDTO(usuario));
    }

    @Operation(summary = "Crear nuevo usuario",
            description = "Crea un nuevo usuario en Keycloak utilizando los datos proporcionados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{ \"response\": \"Usuario creado con éxito\" }"))),
            @ApiResponse(responseCode = "400", description = "Datos de usuario inválidos o incompletos",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "El usuario ya existe",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @PostMapping("/crear")
    public ResponseEntity<String> createUser(
            @Parameter(description = "Datos del nuevo usuario", required = true,
                    schema = @Schema(implementation = UserKeycloakRequestDTO.class))
            @Valid @RequestBody UserKeycloakRequestDTO userKeycloakRequestDTO) throws URISyntaxException {
        String response = keycloakService.createUser(userKeycloakRequestDTO);
        return ResponseEntity.created(new URI("/api/v1/keycloak/usuarios/" + userKeycloakRequestDTO.getUsername()))
                .body(response);
    }

    @Operation(summary = "Actualizar usuario existente",
            description = "Actualiza los datos de un usuario existente en Keycloak.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{ \"response\": \"Usuario actualizado correctamente\"}"))
                            ),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos o incompletos",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @PutMapping("/actualizar/{userId}")
    public ResponseEntity<String> updateUser(@Parameter(description = "ID del usuario a actualizar", required = true, example = "eed47ce8-cfca-4b7c-98ab-11316e830062")
                                        @PathVariable String userId,
                                        @Parameter(description = "Nuevos datos del usuario", required = true,
                                                schema = @Schema(implementation = UserKeycloakRequestDTO.class))
                                        @Valid @RequestBody UserKeycloakRequestDTO userKeycloakRequestDTO) {
        keycloakService.updateUser(userId, userKeycloakRequestDTO);
        return ResponseEntity.ok("Usuario actualizado correctamente");
    }

    @Operation(summary = "Eliminar usuario",
            description = "Elimina un usuario existente de Keycloak.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content)
    })
    @DeleteMapping("/eliminar/{userId}")
    public ResponseEntity<Void> deleteUser(@Parameter(description = "ID del usuario a eliminar", required = true, example = "eed47ce8-cfca-4b7c-98ab-11316e830062")
                                        @PathVariable String userId) {
        keycloakService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Convierte un UserRepresentation de Keycloak a UserKeycloakResponseDTO
     *
     * @param userRepresentation representación del usuario desde Keycloak
     * @return DTO de respuesta del usuario
     */
    private UserKeycloakResponseDTO convertToResponseDTO(UserRepresentation userRepresentation) {
        UserKeycloakResponseDTO dto = new UserKeycloakResponseDTO();
        dto.setId(userRepresentation.getId());
        dto.setUsername(userRepresentation.getUsername());
        dto.setEmail(userRepresentation.getEmail());
        dto.setFirstName(userRepresentation.getFirstName());
        dto.setLastName(userRepresentation.getLastName());
        dto.setEmailVerified(userRepresentation.isEmailVerified());
        dto.setEnabled(userRepresentation.isEnabled());

        // Obtener roles del usuario
        Set<String> roles = keycloakService.getUserRoles(userRepresentation.getId());
        dto.setRoles(roles);

        return dto;
    }



}
