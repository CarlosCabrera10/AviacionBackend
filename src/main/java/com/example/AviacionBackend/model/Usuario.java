package com.example.AviacionBackend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    private Boolean activo;

    private String contrasena; // 👈 Campo que faltaba


    public enum Rol {
        Administrador, Tutor, Alumno
    }
}
