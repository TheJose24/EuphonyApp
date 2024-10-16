package com.euphony.streaming.entity;

import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class UsuarioRolId implements Serializable {
    private Long idUsuario;
    private Long idRol;
}
