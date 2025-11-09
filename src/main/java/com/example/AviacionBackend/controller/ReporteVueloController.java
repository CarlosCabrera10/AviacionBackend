package com.example.AviacionBackend.controller;

import com.example.AviacionBackend.model.ReporteVuelo;
import com.example.AviacionBackend.service.ReporteVueloService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reportes-vuelo")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ReporteVueloController {

    private final ReporteVueloService reporteService;

    @GetMapping
    public List<ReporteVuelo> listar() {
        return reporteService.listar();
    }

    @GetMapping("/{id}")
    public ReporteVuelo obtenerPorId(@PathVariable Long id) {
        return reporteService.obtenerPorId(id);
    }

    @GetMapping("/vuelo/{idVuelo}")
    public List<ReporteVuelo> listarPorVuelo(@PathVariable Long idVuelo) {
        return reporteService.listarPorVuelo(idVuelo);
    }

    @PostMapping
    public ReporteVuelo crear(@RequestBody ReporteVuelo reporte) {
        return reporteService.guardar(reporte);
    }

    @PutMapping("/{id}")
    public ReporteVuelo actualizar(@PathVariable Long id, @RequestBody ReporteVuelo reporte) {
        reporte.setIdReporte(id);
        return reporteService.guardar(reporte);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        reporteService.eliminar(id);
    }
}

