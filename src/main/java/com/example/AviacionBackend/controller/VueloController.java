package com.example.AviacionBackend.controller;

import com.example.AviacionBackend.model.dto.VueloDTO;
import com.example.AviacionBackend.service.VueloService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vuelos")
@RequiredArgsConstructor
public class VueloController {

    private final VueloService vueloService;

    // LISTAR TODOS LOS VUELOS
    @GetMapping
    public List<VueloDTO> listar() {
        return vueloService.listar();
    }

    // OBTENER VUELO POR ID
    @GetMapping("/{id}")
    public VueloDTO obtenerPorId(@PathVariable Long id) {
        return vueloService.obtenerPorId(id);
    }

    // CREAR VUELO
    @PostMapping
    public VueloDTO guardar(@RequestBody VueloDTO dto) {
        return vueloService.guardar(dto);
    }

    // ACTUALIZAR VUELO
    @PutMapping("/{id}")
    public VueloDTO actualizar(@PathVariable Long id, @RequestBody VueloDTO dto) {
        return vueloService.actualizar(id, dto);
    }

    // ELIMINAR VUELO
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        vueloService.eliminar(id);
    }

    // LISTAR VUELOS POR TUTOR
    @GetMapping("/tutor/{idTutor}")
    public List<VueloDTO> listarPorTutor(@PathVariable Long idTutor) {
        return vueloService.listarPorTutor(idTutor);
    }
}
