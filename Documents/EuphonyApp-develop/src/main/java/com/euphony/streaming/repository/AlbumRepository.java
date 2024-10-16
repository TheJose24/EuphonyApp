package com.euphony.streaming.repository;

import com.euphony.streaming.entity.AlbumEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<AlbumEntity, Long> {
    AlbumEntity findByTitulo(String titulo);
    Boolean existsByTitulo(String titulo);
}
