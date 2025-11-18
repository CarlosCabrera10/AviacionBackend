package com.example.AviacionBackend.controller;

import com.example.AviacionBackend.model.EspacioVuelo;
import com.example.AviacionBackend.service.EspacioVueloService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/espacios")
@RequiredArgsConstructor
public class EspacioVueloController {

    private final EspacioVueloService espacioVueloService;

    // Listar todos
    @GetMapping
    public List<EspacioVuelo> listar() {
        return espacioVueloService.listar();
    }

    // Obtener por ID
    @GetMapping("/{id}")
    public EspacioVuelo obtener(@PathVariable Long id) {
        return espacioVueloService.obtenerPorId(id);
    }

    // Actualizar
    @PutMapping("/{id}")
    public EspacioVuelo actualizar(@PathVariable Long id, @RequestBody EspacioVuelo datos) {
        return espacioVueloService.actualizar(id, datos);
    }

    // Desactivar
    @PutMapping("/{id}/desactivar")
    public EspacioVuelo desactivar(@PathVariable Long id) {
        return espacioVueloService.desactivar(id);
    }

    // Activar
    @PutMapping("/{id}/activar")
    public EspacioVuelo activar(@PathVariable Long id) {
        return espacioVueloService.activar(id);
    }
}
