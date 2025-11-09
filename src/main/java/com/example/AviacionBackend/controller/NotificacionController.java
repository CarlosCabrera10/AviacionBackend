package com.example.AviacionBackend.controller;

import com.example.AviacionBackend.model.Notificacion;
import com.example.AviacionBackend.service.NotificacionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin(origins = "*")
public class NotificacionController {

    private final NotificacionService service;

    public NotificacionController(NotificacionService service) {
        this.service = service;
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<Notificacion> listarPorUsuario(@PathVariable Long idUsuario) {
        return service.listarPorUsuario(idUsuario);
    }

    @GetMapping("/usuario/{idUsuario}/noleidas")
    public List<Notificacion> listarNoLeidas(@PathVariable Long idUsuario) {
        return service.listarNoLeidas(idUsuario);
    }

    @PostMapping
    public Notificacion crear(@RequestBody Notificacion notificacion) {
        return service.crear(notificacion);
    }

    @PutMapping("/{id}/leida")
    public Notificacion marcarComoLeida(@PathVariable Long id) {
        return service.marcarComoLeida(id).orElseThrow(() -> new RuntimeException("Notificación no encontrada"));
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
