package com.example.AviacionBackend.controller;

import com.example.AviacionBackend.model.Vuelo;
import com.example.AviacionBackend.service.VueloService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vuelos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class VueloController {

    private final VueloService vueloService;

    @GetMapping
    public List<Vuelo> listar() {
        return vueloService.listar();
    }

    @GetMapping("/{id}")
    public Vuelo obtenerPorId(@PathVariable Long id) {
        return vueloService.obtenerPorId(id);
    }

    @PostMapping
    public Vuelo crear(@RequestBody Vuelo vuelo) {
        return vueloService.guardar(vuelo);
    }

    @PutMapping("/{id}")
    public Vuelo actualizar(@PathVariable Long id, @RequestBody Vuelo vuelo) {
        vuelo.setIdVuelo(id);
        return vueloService.guardar(vuelo);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        vueloService.eliminar(id);
    }

    // 🔥 Listar vuelos por tutor
    @GetMapping("/tutor/{idTutor}")
    public List<Vuelo> listarPorTutor(@PathVariable Long idTutor) {
        return vueloService.listarPorTutor(idTutor);
    }
}
