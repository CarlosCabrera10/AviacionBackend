package com.example.AviacionBackend.model.dto;

import com.example.AviacionBackend.model.Vuelo;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class VueloDTO {

    private Long idVuelo;

    // IDs para relacionar
    private Long idAlumno;
    private Long idTutor;
    private Long idAvioneta;

    // Campos legibles para mostrar en el frontend
    private String nombreAlumno;
    private String nombreTutor;
    private String codigoAvioneta;

    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Vuelo.Estado estado;
    private String observacion;
}
