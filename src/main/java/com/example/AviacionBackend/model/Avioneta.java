package com.example.AviacionBackend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "avioneta")
public class Avioneta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAvioneta;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false)
    private String modelo;

    @Column(name = "horas_vuelo", columnDefinition = "DECIMAL(6,2) DEFAULT 0")
    private Double horasVuelo = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoAvioneta estado = EstadoAvioneta.Activo;

    public enum EstadoAvioneta {
        Activo,
        Mantenimiento
    }
}
