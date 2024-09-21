package com.euphony.streaming.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "PLANES_SUSCRIPCION")
public class PlanesSuscripcionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plan")
    private Long idPlan;

    @Column(name = "nombre_plan", nullable = false, unique = true, length = 100)
    private String nombrePlan;

    @Column(name = "precio", nullable = false)
    private Double precio;

    @Column(name = "duracion", nullable = false)
    private Integer duracion;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

}
