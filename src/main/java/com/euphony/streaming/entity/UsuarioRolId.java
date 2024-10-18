package com.euphony.streaming.entity;

import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.UUID;

@EqualsAndHashCode
public class UsuarioRolId implements Serializable {
    private UUID idUsuario;
    private Long idRol;
}
