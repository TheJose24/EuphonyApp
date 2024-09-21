package com.euphony.streaming.repository;

import com.euphony.streaming.entity.InformacionUsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InformacionUsuarioRepository extends JpaRepository<InformacionUsuarioEntity, Long> {
}
