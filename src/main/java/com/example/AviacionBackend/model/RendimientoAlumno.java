package com.example.AviacionBackend.model;


import jakarta.persistence.*;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "rendimiento_alumno")
public class RendimientoAlumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rendimiento")
    private Long idRendimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vuelo", nullable = false)
    private Vuelo vuelo;

    @Column(name = "puntaje_general", precision = 4, scale = 2)
    private BigDecimal puntajeGeneral;

    @Column(name = "tecnica_aterrizaje", precision = 4, scale = 2)
    private BigDecimal tecnicaAterrizaje;

    @Column(name = "maniobras", precision = 4, scale = 2)
    private BigDecimal maniobras;

    @Column(name = "comunicacion_radio", precision = 4, scale = 2)
    private BigDecimal comunicacionRadio;

    @Column(name = "seguimiento_instrucciones", precision = 4, scale = 2)
    private BigDecimal seguimientoInstrucciones;

    @Column(name = "puntualidad", precision = 4, scale = 2)
    private BigDecimal puntualidad;

    @Column(name = "comportamiento", precision = 4, scale = 2)
    private BigDecimal comportamiento;

    @Column(columnDefinition = "TEXT")
    private String comentarios;

    @Column(name = "fecha", columnDefinition = "DATETIME")
    private LocalDateTime fecha;
}
