package com.euphony.streaming.service.implementation;

import com.euphony.streaming.dto.request.PlaylistRequestDTO;
import com.euphony.streaming.dto.response.PlaylistResponseDTO;
import com.euphony.streaming.entity.CancionEntity;
import com.euphony.streaming.entity.PlaylistCancionEntity;
import com.euphony.streaming.entity.PlaylistEntity;
import com.euphony.streaming.entity.UsuarioEntity;
import com.euphony.streaming.exception.custom.PlaylistNotFoundException;
import com.euphony.streaming.repository.CancionRepository;
import com.euphony.streaming.repository.PlaylistCancionRepository;
import com.euphony.streaming.repository.PlaylistRepository;
import com.euphony.streaming.repository.UsuarioRepository;
import com.euphony.streaming.service.interfaces.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final PlaylistCancionRepository playlistCancionRepository;
    private final CancionRepository cancionRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public PlaylistResponseDTO createPlaylist(PlaylistRequestDTO playlistRequestDTO) {
        UsuarioEntity usuario = usuarioRepository.findById(Long.valueOf(playlistRequestDTO.getIdUsuario()))
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        PlaylistEntity playlist = new PlaylistEntity();
        playlist.setNombre(playlistRequestDTO.getNombre());
        playlist.setDescripcion(playlistRequestDTO.getDescripcion());
        playlist.setIsPublic(playlistRequestDTO.getIsPublic());
        playlist.setFechaCreacion(LocalDate.now());
        playlist.setUsuario(usuario);

        PlaylistEntity savedPlaylist = playlistRepository.save(playlist);
        return convertToDTO(savedPlaylist);
    }
    @Override
    public PlaylistResponseDTO getPlaylistById(Long id) {
        PlaylistEntity playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist no encontrada con id: " + id));
        return convertToDTO(playlist);
    }

    @Override
    @Transactional
    public PlaylistResponseDTO addSongToPlaylist(Long playlistId, Long songId) {
        PlaylistEntity playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist no encontrada"));
        CancionEntity cancion = cancionRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Canción no encontrada"));

        PlaylistCancionEntity playlistCancion = new PlaylistCancionEntity();
        playlistCancion.setPlaylist(playlist);
        playlistCancion.setCancion(cancion);
        playlistCancionRepository.save(playlistCancion);

        return convertToDTO(playlist);
    }

    @Override
    @Transactional
    public PlaylistResponseDTO removeSongFromPlaylist(Long playlistId, Long songId) {
        PlaylistEntity playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist no encontrada"));
        CancionEntity cancion = cancionRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Canción no encontrada"));

        PlaylistCancionEntity playlistCancion = playlistCancionRepository
                .findByPlaylistAndCancion(playlist, cancion)
                .orElseThrow(() -> new RuntimeException("La canción no está en la playlist"));

        playlistCancionRepository.delete(playlistCancion);

        return convertToDTO(playlist);
    }

    @Override
    public List<PlaylistResponseDTO> getAllPlaylists() {
        List<PlaylistEntity> playlists = playlistRepository.findAll();
        return playlists.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PlaylistResponseDTO updatePlaylist(Long id, PlaylistRequestDTO playlistRequestDTO) {
        PlaylistEntity playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist no encontrada"));

        playlist.setNombre(playlistRequestDTO.getNombre());
        playlist.setDescripcion(playlistRequestDTO.getDescripcion());
        playlist.setIsPublic(playlistRequestDTO.getIsPublic());

        PlaylistEntity updatedPlaylist = playlistRepository.save(playlist);
        return convertToDTO(updatedPlaylist);
    }

    @Override
    @Transactional
    public void deletePlaylist(Long id) {
        if (!playlistRepository.existsById(id)) {
            throw new PlaylistNotFoundException("Playlist no encontrada");
        }
        playlistRepository.deleteById(id);
    }

    private PlaylistResponseDTO convertToDTO(PlaylistEntity playlist) {
        PlaylistResponseDTO dto = new PlaylistResponseDTO();
        dto.setIdPlaylist(String.valueOf(playlist.getIdPlaylist()));
        dto.setNombre(playlist.getNombre());
        dto.setDescripcion(playlist.getDescripcion());
        dto.setIsPublic(playlist.getIsPublic());
        dto.setFechaCreacion(playlist.getFechaCreacion());
        dto.setIdUsuario(String.valueOf(playlist.getUsuario().getIdUsuario()));

        List<String> idsCanciones = playlistCancionRepository.findByPlaylist(playlist)
                .stream()
                .map(pc -> String.valueOf(pc.getCancion().getIdCancion()))
                .collect(Collectors.toList());
        dto.setIdsCanciones(idsCanciones);

        return dto;
    }
}