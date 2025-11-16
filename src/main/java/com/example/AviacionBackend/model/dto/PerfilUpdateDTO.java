package com.example.AviacionBackend.model.dto;


import lombok.Data;

@Data
public class PerfilUpdateDTO {


    private String correo;
    private String telefono;
    private String nuevaContrasena;
    private boolean puedeEditarNombre;
    private String nombre;
    private String apellido;
}
