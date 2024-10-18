package com.euphony.streaming.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@IdClass(UsuarioRolId.class)
@Entity
@Table(name = "USUARIO_ROL")
public class UsuarioRolEntity {

    @Id
    @Column(name = "id_usuario")
    private UUID idUsuario;

    @Id
    @Column(name = "id_rol")
    private Long idRol;

    @ManyToOne
    @JoinColumn(name = "id_usuario", insertable = false, updatable = false)
    private UsuarioEntity usuario;

    @ManyToOne
    @JoinColumn(name = "id_rol", insertable = false, updatable = false)
    private RolEntity rol;

}
