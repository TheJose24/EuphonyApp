package com.euphony.streaming.repository;

import com.euphony.streaming.entity.CancionEntity;
import com.euphony.streaming.entity.PlaylistCancionEntity;
import com.euphony.streaming.entity.PlaylistCancionId;
import com.euphony.streaming.entity.PlaylistEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaylistCancionRepository extends JpaRepository<PlaylistCancionEntity, PlaylistCancionId> {

    List<PlaylistCancionEntity> findByPlaylist(PlaylistEntity playlist);

    Optional<PlaylistCancionEntity> findByPlaylistAndCancion(PlaylistEntity playlist, CancionEntity cancion);
}