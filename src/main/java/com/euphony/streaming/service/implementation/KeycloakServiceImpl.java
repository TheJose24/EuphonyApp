package com.euphony.streaming.service.implementation;

import com.euphony.streaming.dto.request.UserKeycloakRequestDTO;
import com.euphony.streaming.dto.response.UserKeycloakResponseDTO;
import com.euphony.streaming.exception.custom.UserCreationException;
import com.euphony.streaming.exception.custom.UserNotFoundException;
import com.euphony.streaming.service.interfaces.IKeycloakService;
import com.euphony.streaming.util.KeycloakProvider;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class KeycloakServiceImpl implements IKeycloakService {

    private static final String CLIENT_ID = "euphony-client";

    /**
     * Recupera una lista de todos los usuarios de Keycloak.
     *
     * @return una lista de objetos UserKeycloakResponeDTO que representan a todos los usuarios del ámbito Keycloak.
     */
    @Override
    public List<UserRepresentation> findAllUsers() {
        return KeycloakProvider.getRealmResource()
                .users()
                .list();
    }

    /**
     * Busca un usuario por su nombre de usuario en el ámbito Keycloak.
     *
     *  @param username el nombre de usuario del usuario que se buscará
     *  @return una lista de objetos UserRepresentation que representan a los usuarios encontrados
     */
    @Override
    public UserRepresentation searchUserByUsername(String username) {
        validateNotEmpty(username, "El nombre de usuario no puede estar vacío");

        return KeycloakProvider.getRealmResource()
                .users()
                .searchByUsername(username, true)
                .stream()
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
    }

    /**
     * Crea un nuevo usuario en Keycloak.
     *
     * @param userKeycloakRequestDTO el objeto DTO que contiene la información del usuario a crear
     * @return un mensaje indicando el resultado de la operación
     */
    @Override
    public String createUser(@NotNull UserKeycloakRequestDTO userKeycloakRequestDTO) {
        validateUserInput(userKeycloakRequestDTO);

        UsersResource userResource = KeycloakProvider.getUsersResource();
        UserRepresentation userRepresentation = buildUserRepresentation(userKeycloakRequestDTO);

        try (Response response = userResource.create(userRepresentation)) {
            int status = response.getStatus();

            if (status == 201) {

                String userId = extractUserId(response);
                setUserPassword(userId, userKeycloakRequestDTO.getPassword());
                assignUserRoles(userId, userKeycloakRequestDTO.getRoles());

                log.info("Usuario creado con éxito: {}", userKeycloakRequestDTO.getUsername());
                return "Usuario creado con éxito";

            } else if (status == 409) {
                log.error("El usuario ya existe: {}", userKeycloakRequestDTO.getUsername());
                throw new UserCreationException("El usuario ya existe");
            } else {
                log.error("Error al crear el usuario: {}. Status: {}. Detalles: {}", userKeycloakRequestDTO.getUsername(), status, response.getEntity());
                throw new UserCreationException("Error al crear el usuario, por favor contacte al administrador");
            }
        }
    }


    /**
     * Elimina un usuario de Keycloak.
     *
     * @param userId el ID del usuario que se eliminará
     */
    @Override
    public void deleteUser(String userId) {

        validateNotEmpty(userId, "El ID de usuario no puede estar vacío");

        try{
            KeycloakProvider.getUsersResource()
                    .get(userId)
                    .remove();
            log.info("Usuario eliminado con éxito: {}", userId);
        } catch (NotFoundException e){
            log.error("Usuario no encontrado para eliminar: {}", userId);
            throw new UserNotFoundException("Usuario no encontrado");
        }

    }

    /**
     * Actualiza un usuario en Keycloak.
     *
     * @param userId el ID del usuario que se actualizará
     * @param userKeycloakRequestDTO el objeto DTO que contiene la información del usuario a actualizar
     */
    @Override
    public void updateUser(String userId, @NotNull UserKeycloakRequestDTO userKeycloakRequestDTO) {

        validateNotEmpty(userId, "El ID de usuario no puede estar vacío");

        validateUserInput(userKeycloakRequestDTO);

        UserResource userResource = KeycloakProvider.getUsersResource().get(userId);
        UserRepresentation userRepresentation = buildUserRepresentation(userKeycloakRequestDTO);

        try {
            userResource.update(userRepresentation);
            setUserPassword(userId, userKeycloakRequestDTO.getPassword());

            log.info("Usuario actualizado con éxito: {}", userKeycloakRequestDTO.getUsername());
        } catch (NotFoundException e){
            log.error("Usuario no encontrado: {}", userId);
            throw new UserNotFoundException("Usuario no encontrado");
        }
    }

    /**
     * Valida la entrada del usuario para crear o actualizar un usuario en Keycloak.
     *
     * @param userKeycloakRequestDTO el DTO que contiene la información del usuario que se debe validar
     * @throws BadRequestException si falta algún campo obligatorio o está vacío
     */
    private void validateUserInput(UserKeycloakRequestDTO userKeycloakRequestDTO) {
        validateNotEmpty(userKeycloakRequestDTO.getUsername(), "El nombre de usuario es obligatorio");
        validateNotEmpty(userKeycloakRequestDTO.getEmail(), "El email es obligatorio");
        validateNotEmpty(userKeycloakRequestDTO.getPassword(), "La contraseña es obligatoria");
    }

    private void validateNotEmpty(String value, String errorMessage) {
        if (value == null || value.trim().isEmpty()) {
            throw new BadRequestException(errorMessage);
        }
    }

    /**
     * Extrae el ID de usuario de la ruta de ubicación de respuesta.
     *
     * @param response el objeto de respuesta que contiene el encabezado de ubicación
     * @return el ID de usuario extraído
     */
    private String extractUserId(Response response) {
        String path = response.getLocation().getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    /**
     * Establece la contraseña de un usuario en Keycloak.
     *
     * @param userId el ID del usuario cuya contraseña se establecerá
     * @param password la nueva contraseña que se establecerá para el usuario
     */
    private void setUserPassword(String userId, String password) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(OAuth2Constants.PASSWORD);
        credentialRepresentation.setValue(password);

        KeycloakProvider.getUsersResource().get(userId).resetPassword(credentialRepresentation);
    }

    /**
     * Asigna roles a un usuario en Keycloak.
     *
     * @param userId el ID del usuario al que se le asignarán los roles
     * @param roles un conjunto de nombres de roles que se asignarán al usuario
     */
    private void assignUserRoles(String userId, Set<String> roles) {
        RealmResource realmResource = KeycloakProvider.getRealmResource();
        String clientUuid = getClientUuid(realmResource);

        if (clientUuid == null) return;

        List<RoleRepresentation> roleRepresentations = roles == null || roles.isEmpty()
                ? List.of(realmResource.clients().get(clientUuid).roles().get("user_client_role").toRepresentation())
                : getRoleRepresentations(realmResource, clientUuid, roles);

        realmResource.users().get(userId).roles().clientLevel(clientUuid).add(roleRepresentations);
    }

    /**
     * Recupera el UUID del cliente con el ID de cliente especificado.
     *
     * @param realmResource el RealmResource para interactuar con el reino Keycloak
     * @return el UUID del cliente, o null si el cliente no existe
     */
    private String getClientUuid(RealmResource realmResource) {
        var clients = realmResource.clients().findByClientId(CLIENT_ID);
        if (clients.isEmpty()) {
            log.error("El cliente '{}' no existe en el realm.", CLIENT_ID);
            return null;
        }
        return clients.get(0).getId();
    }

    /**
     * Recupera una lista de objetos RoleRepresentation para los roles especificados.
     *
     * @param realmResource el RealmResource para interactuar con el dominio Keycloak
     * @param clientUuid el UUID del cliente
     * @param roles un conjunto de nombres de roles para recuperar
     * @return una lista de objetos RoleRepresentation que coinciden con los roles especificados
     */
    private List<RoleRepresentation> getRoleRepresentations(RealmResource realmResource, String clientUuid, Set<String> roles) {
        return realmResource.clients().get(clientUuid).roles().list().stream()
                .filter(role -> roles.contains(role.getName()))
                .collect(Collectors.toList());
    }

    /**
     * Recupera los roles asignados a un usuario en Keycloak.
     *
     * @param userId el ID del usuario cuyos roles se recuperarán
     * @return un conjunto de nombres de roles asignados al usuario
     */
    @Override
    public Set<String> getUserRoles(String userId) {
        var clientMappings = KeycloakProvider.getUserRolesResource(userId);

        if (clientMappings != null && clientMappings.containsKey(CLIENT_ID)) {
            return clientMappings.get(CLIENT_ID).getMappings().stream()
                    .map(RoleRepresentation::getName)
                    .collect(Collectors.toSet());
        } else {
            log.info("No se encontraron roles para el cliente '{}' para el usuario con ID: {}", CLIENT_ID, userId);
            return Set.of();
        }
    }

    /**
     * Crea un objeto UserRepresentation a partir del UserKeycloakRequestDTO proporcionado.
     *
     * @param userKeycloakRequestDTO el DTO que contiene la información del usuario
     * @return un objeto UserRepresentation con la información del usuario
     */
    private UserRepresentation buildUserRepresentation(UserKeycloakRequestDTO userKeycloakRequestDTO) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(userKeycloakRequestDTO.getFirstName());
        userRepresentation.setLastName(userKeycloakRequestDTO.getLastName());
        userRepresentation.setEmail(userKeycloakRequestDTO.getEmail());
        userRepresentation.setUsername(userKeycloakRequestDTO.getUsername());
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);
        return userRepresentation;
    }

}
