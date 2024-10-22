package com.euphony.streaming.service.interfaces;

import com.euphony.streaming.dto.request.UserProfileRequestDTO;
import com.euphony.streaming.dto.response.UserProfileResponseDTO;
import com.euphony.streaming.entity.PerfilUsuarioEntity;

import java.util.List;
import java.util.UUID;

/**
 * Interfaz que define las operaciones relacionadas con la gestión de perfiles de usuarios.
 */
public interface IProfileUserService {

    /**
     * Obtiene una lista de todos los perfiles de usuario registrados en el sistema.
     *
     * @return Una lista de {@link UserProfileResponseDTO} que contiene los datos de todos los perfiles de usuario.
     */
    List<UserProfileResponseDTO> findAllProfiles();

    /**
     * Obtiene la información de un perfil de usuario específico a partir de su ID.
     *
     * @param usuarioId El identificador único del usuario.
     * @return Un {@link UserProfileResponseDTO} con los datos del perfil de usuario.
     */
    UserProfileResponseDTO searchProfileByUsuarioId(UUID usuarioId);

    /**
     * Elimina un perfil de usuario del sistema a partir de su ID.
     *
     * @param userId El identificador único del usuario que se desea eliminar.
     */
    void deleteProfile(UUID userId);


    /**
     * Actualiza los datos de un perfil de usuario existente a partir de su ID.
     *
     * @param userId El identificador único del usuario que se desea actualizar.
     * @param profileUsuarioRequestDTO Un objeto {@link UserProfileRequestDTO} con los datos actualizados del perfil de usuario.
     */
    void updateProfile(UUID userId, UserProfileRequestDTO profileUsuarioRequestDTO);

}
