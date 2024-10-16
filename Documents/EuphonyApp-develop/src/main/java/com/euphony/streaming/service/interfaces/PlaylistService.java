package com.euphony.streaming.service.interfaces;

import com.euphony.streaming.dto.request.PlaylistRequestDTO;
import com.euphony.streaming.dto.response.PlaylistResponseDTO;

import java.util.List;

public interface PlaylistService {
    PlaylistResponseDTO createPlaylist(PlaylistRequestDTO playlistRequestDTO);
    PlaylistResponseDTO getPlaylistById(Long id);
    List<PlaylistResponseDTO> getAllPlaylists();
    PlaylistResponseDTO updatePlaylist(Long id, PlaylistRequestDTO playlistRequestDTO);
    void deletePlaylist(Long id);
    PlaylistResponseDTO addSongToPlaylist(Long playlistId, Long songId);
    PlaylistResponseDTO removeSongFromPlaylist(Long playlistId, Long songId);
}