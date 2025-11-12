package com.example.AviacionBackend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "vuelo")
public class Vuelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vuelo")
    private Long idVuelo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_alumno", nullable = false)
    private Usuario alumno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tutor", nullable = false)
    private Usuario tutor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_avioneta", nullable = false)
    private Avioneta avioneta;

    private LocalDate fecha;
    private LocalTime hora;

    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.Programado;

    private String observacion;

    public enum Estado {
        Programado,
        Completado,
        Cancelado
    }
}
