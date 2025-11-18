package com.example.AviacionBackend.controller;

import com.example.AviacionBackend.model.dto.RendimientoAlumnoDTO;
import com.example.AviacionBackend.service.RendimientoAlumnoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rendimientos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class RendimientoAlumnoController {

    private final RendimientoAlumnoService rendimientoService;

    // Obtener rendimiento por vuelo (para mostrar si ya existe)
    @GetMapping("/vuelo/{idVuelo}")
    public RendimientoAlumnoDTO obtenerPorVuelo(@PathVariable Long idVuelo) {
        return rendimientoService.obtenerPorVuelo(idVuelo);
    }

    // Crear/actualizar rendimiento de un vuelo
    @PostMapping("/vuelo/{idVuelo}")
    public RendimientoAlumnoDTO guardarOActualizar(
            @PathVariable Long idVuelo,
            @RequestBody RendimientoAlumnoDTO dto
    ) {
        return rendimientoService.guardarOActualizar(idVuelo, dto);
    }
}