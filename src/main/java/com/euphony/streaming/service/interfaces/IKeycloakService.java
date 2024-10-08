package com.euphony.streaming.service.interfaces;

import com.euphony.streaming.dto.request.UserKeycloakRequestDTO;
import com.euphony.streaming.dto.response.UserKeycloakResponseDTO;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;
import java.util.Set;

/**
 * Servicio para gestionar usuarios en Keycloak.
 * Este servicio maneja la lógica de negocio relacionada con la creación, búsqueda, actualización y eliminación
 * de usuarios en Keycloak.
 */
public interface IKeycloakService {

    /**
     * Recupera una lista de todos los usuarios de Keycloak.
     *
     * @return una lista de objetos UserRepresentation que representan a todos los usuarios del ámbito Keycloak.
     */
    List<UserRepresentation> findAllUsers();

    /**
     * Busca un usuario por su nombre de usuario en Keycloak.
     *
     * @param username el nombre de usuario que se buscará
     * @return una lista de objetos UserRepresentation que representan a los usuarios encontrados
     */
    UserRepresentation searchUserByUsername(String username);

    /**
     * Crea un nuevo usuario en Keycloak.
     *
     * @param userKeycloakRequestDTO el objeto DTO que contiene la información del usuario a crear
     * @return un mensaje indicando el resultado de la operación
     */
    String createUser(UserKeycloakRequestDTO userKeycloakRequestDTO);

    /**
     * Elimina un usuario de Keycloak.
     *
     * @param userId el ID del usuario que se eliminará
     */
    void deleteUser(String userId);

    /**
     * Actualiza los datos de un usuario en Keycloak.
     *
     * @param userId el ID del usuario que se actualizará
     * @param userKeycloakRequestDTO el objeto DTO que contiene los datos actualizados del usuario
     */
    void updateUser(String userId, UserKeycloakRequestDTO userKeycloakRequestDTO);

    /**
     * Recupera los nombres de los roles asignados a un usuario en Keycloak para un cliente específico.
     *
     * @param userId el ID del usuario del que se obtendrán los roles.
     * @return una lista de nombres de roles asignados al usuario.
     */
    Set<String> getUserRoles(String userId);
}
