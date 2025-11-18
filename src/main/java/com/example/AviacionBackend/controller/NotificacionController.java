package com.example.AviacionBackend.controller;

import com.example.AviacionBackend.model.Notificacion;
import com.example.AviacionBackend.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class NotificacionController {

    private final NotificacionService service;

    @GetMapping("/{idUsuario}")
    public List<Notificacion> listar(@PathVariable Long idUsuario) {
        return service.obtenerPorUsuario(idUsuario);
    }

    @PutMapping("/leer/{id}")
    public void marcarLeida(@PathVariable Long id) {
        service.marcarLeida(id);
    }
}
