package com.euphony.streaming.repository;

import com.euphony.streaming.entity.ArtistaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistaRepository extends JpaRepository<ArtistaEntity, Long> {
}
