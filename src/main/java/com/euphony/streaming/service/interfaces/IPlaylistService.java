package com.euphony.streaming.service.interfaces;

import com.euphony.streaming.dto.request.PlaylistRequestDTO;
import com.euphony.streaming.dto.response.PlaylistResponseDTO;
import java.util.List;

/**
 * Interfaz que define las operaciones relacionadas con la gestión de playlists.
 */
public interface IPlaylistService {

    /**
     * Obtiene todas las playlists registradas en el sistema.
     *
     * @return Una lista de {@link PlaylistResponseDTO} que contiene los datos de todas las playlists.
     */
    List<PlaylistResponseDTO> findAllPlaylists();

    /**
     * Obtiene la información de una playlist específica a partir de su ID.
     *
     * @param id El identificador único de la playlist.
     * @return Un {@link PlaylistResponseDTO} con los datos de la playlist.
     */
    PlaylistResponseDTO findPlaylistById(Long id);

    /**
     * Crea una nueva playlist en el sistema a partir de los datos proporcionados.
     *
     * @param playlistRequestDTO Un objeto {@link PlaylistRequestDTO} que contiene los datos de la nueva playlist.
     * @return Un {@link PlaylistResponseDTO} con los datos de la playlist creada.
     */
    PlaylistResponseDTO createPlaylist(PlaylistRequestDTO playlistRequestDTO);

    /**
     * Actualiza los datos de una playlist existente a partir de su ID.
     *
     * @param id El identificador único de la playlist que se desea actualizar.
     * @param playlistRequestDTO Un objeto {@link PlaylistRequestDTO} con los datos actualizados de la playlist.
     * @return Un {@link PlaylistResponseDTO} con los datos de la playlist actualizada.
     */
    PlaylistResponseDTO updatePlaylist(Long id, PlaylistRequestDTO playlistRequestDTO);

    /**
     * Elimina una playlist del sistema a partir de su ID.
     *
     * @param id El identificador único de la playlist que se desea eliminar.
     */
    void deletePlaylist(Long id);

    /**
     * Agrega una canción a una playlist existente.
     *
     * @param playlistId El identificador único de la playlist.
     * @param cancionId El identificador único de la canción que se desea agregar.
     * @return Un {@link PlaylistResponseDTO} con los datos de la playlist actualizada.
     */
    PlaylistResponseDTO addSongToPlaylist(Long playlistId, Long cancionId);

    /**
     * Elimina una canción de una playlist existente.
     *
     * @param playlistId El identificador único de la playlist.
     * @param cancionId El identificador único de la canción que se desea eliminar.
     * @return Un {@link PlaylistResponseDTO} con los datos de la playlist actualizada.
     */
    PlaylistResponseDTO removeSongFromPlaylist(Long playlistId, Long cancionId);
}