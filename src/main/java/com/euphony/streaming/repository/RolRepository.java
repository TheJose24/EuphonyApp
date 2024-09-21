package com.euphony.streaming.repository;

import com.euphony.streaming.entity.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<RolEntity, Long> {
    RolEntity findByNombreRol(String nombreRol);
}
