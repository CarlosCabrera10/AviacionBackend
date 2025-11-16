package com.example.AviacionBackend.controller;

import com.example.AviacionBackend.model.Mantenimiento;
import com.example.AviacionBackend.service.MantenimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mantenimientos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MantenimientoController {

    private final MantenimientoService mantenimientoService;

    @GetMapping
    public List<Mantenimiento> listar() {
        return mantenimientoService.listar();
    }

    @GetMapping("/{id}")
    public Mantenimiento obtenerPorId(@PathVariable Long id) {
        return mantenimientoService.obtenerPorId(id);
    }

    @PostMapping
    public Mantenimiento guardar(@RequestBody Mantenimiento datos) {
        return mantenimientoService.guardar(datos);
    }

    @PutMapping("/{id}")
    public Mantenimiento actualizar(@PathVariable Long id, @RequestBody Mantenimiento datos) {
        return mantenimientoService.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        mantenimientoService.eliminar(id);
    }

    @GetMapping("/avioneta/{idAvioneta}")
    public List<Mantenimiento> listarPorAvioneta(@PathVariable Long idAvioneta) {
        return mantenimientoService.listarPorAvioneta(idAvioneta);
    }
}
