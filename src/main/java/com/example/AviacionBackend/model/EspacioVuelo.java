package com.example.AviacionBackend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "espacio_vuelo")
public class EspacioVuelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_espacio")
    private Long idEspacioVuelo;

    private String nombre;
    private String tipo;
    private String ubicacion;
    private String descripcion;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean habilitado = true;
}
