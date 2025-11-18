package com.example.AviacionBackend.model.dto;

import com.example.AviacionBackend.model.Vuelo;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class VueloDTO {

    private Long idVuelo;

    // IDs para relaciones
    private Long idAlumno;
    private Long idTutor;
    private Long idAvioneta;
    private Long idEspacioVuelo;

    // Campos para mostrar en frontend
    private String nombreAlumno;
    private String nombreTutor;
    private String codigoAvioneta;
    private String nombreEspacio;

    // Estado REAL de la avioneta (String)
    private String estadoAvioneta;

    // Datos del vuelo
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Vuelo.Estado estado;

    private String observacion;
}
