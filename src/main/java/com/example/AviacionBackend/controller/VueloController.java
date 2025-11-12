package com.example.AviacionBackend.controller;

import com.example.AviacionBackend.model.dto.VueloDTO;
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

    // Listar todos los vuelos como DTO
    @GetMapping
    public List<VueloDTO> listar() {
        return vueloService.listar();
    }

    // Obtener un vuelo por ID como DTO
    @GetMapping("/{id}")
    public VueloDTO obtenerPorId(@PathVariable Long id) {
        return vueloService.obtenerPorId(id);
    }

    // Crear un vuelo usando DTO
    @PostMapping
    public VueloDTO crear(@RequestBody VueloDTO dto) {
        return vueloService.guardar(dto);
    }

    // Actualizar un vuelo usando DTO
    @PutMapping("/{id}")
    public VueloDTO actualizar(@PathVariable Long id, @RequestBody VueloDTO dto) {
        return vueloService.actualizar(id, dto);
    }

    // Eliminar vuelo
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        vueloService.eliminar(id);
    }

    // Listar vuelos por tutor
    @GetMapping("/tutor/{idTutor}")
    public List<VueloDTO> listarPorTutor(@PathVariable Long idTutor) {
        return vueloService.listarPorTutor(idTutor);
    }
}
