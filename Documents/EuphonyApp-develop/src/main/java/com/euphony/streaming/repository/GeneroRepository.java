package com.euphony.streaming.repository;

import com.euphony.streaming.entity.GeneroEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeneroRepository extends JpaRepository<GeneroEntity, Long> {
    GeneroEntity findByNombre(String nombre);
    Boolean existsByNombre(String nombre);
}
