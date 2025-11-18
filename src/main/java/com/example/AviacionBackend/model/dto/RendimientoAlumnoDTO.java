package com.example.AviacionBackend.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RendimientoAlumnoDTO {


    private Long idRendimiento;
    private Long idVuelo;

    private BigDecimal puntajeGeneral;
    private BigDecimal tecnicaAterrizaje;
    private BigDecimal maniobras;
    private BigDecimal comunicacionRadio;
    private BigDecimal seguimientoInstrucciones;
    private BigDecimal puntualidad;
    private BigDecimal comportamiento;

    private String comentarios;
    private LocalDateTime fecha;
}
