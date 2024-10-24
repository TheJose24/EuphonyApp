package com.euphony.streaming.service.interfaces;

import com.euphony.streaming.dto.request.PlaylistRequestDTO;
import com.euphony.streaming.dto.response.PlaylistResponseDTO;
import java.util.List;

/**
 * Interface that defines playlist management operations.
 */
public interface IPlaylistService {

    /**
     * Gets all playlists registered in the system.
     *
     * @return A list of {@link PlaylistResponseDTO} containing data of all playlists.
     */
    List<PlaylistResponseDTO> findAllPlaylists();

    /**
     * Gets information of a specific playlist by its ID.
     *
     * @param id The unique identifier of the playlist.
     * @return A {@link PlaylistResponseDTO} with playlist data.
     */
    PlaylistResponseDTO findPlaylistById(Long id);

    /**
     * Creates a new playlist in the system.
     *
     * @param playlistRequestDTO A {@link PlaylistRequestDTO} object containing new playlist data.
     */
    void createPlaylist(PlaylistRequestDTO playlistRequestDTO);

    /**
     * Updates data of an existing playlist.
     *
     * @param id The unique identifier of the playlist to update.
     * @param playlistRequestDTO A {@link PlaylistRequestDTO} object with updated playlist data.
     */
    void updatePlaylist(Long id, PlaylistRequestDTO playlistRequestDTO);

    /**
     * Deletes a playlist from the system.
     *
     * @param id The unique identifier of the playlist to delete.
     */
    void deletePlaylist(Long id);

    /**
     * Adds a song to an existing playlist.
     *
     * @param playlistId The unique identifier of the playlist.
     * @param cancionId The unique identifier of the song to add.
     */
    void addSongToPlaylist(Long playlistId, Long cancionId);

    /**
     * Removes a song from an existing playlist.
     *
     * @param playlistId The unique identifier of the playlist.
     * @param cancionId The unique identifier of the song to remove.
     */
    void removeSongFromPlaylist(Long playlistId, Long cancionId);
}