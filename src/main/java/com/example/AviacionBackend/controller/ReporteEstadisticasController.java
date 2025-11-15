package com.example.AviacionBackend.controller;

import com.example.AviacionBackend.service.ReporteEstadisticasService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReporteEstadisticasController {

    private final ReporteEstadisticasService estadisticaService;

    // 1️⃣ Actividad por día
    @GetMapping("/actividad-por-dia")
    public Map<?, Long> actividadPorDia() {
        return estadisticaService.actividadPorDia();
    }

    // 2️⃣ Actividad por hora
    @GetMapping("/actividad-por-hora")
    public Map<?, Long> actividadPorHora() {
        return estadisticaService.actividadPorHora();
    }

    // 3️⃣ Uso de avionetas
    @GetMapping("/uso-avionetas")
    public Map<?, Long> usoAvionetas() {
        return estadisticaService.usoAvionetas();
    }

    // 4️⃣ Tutores activos
    @GetMapping("/tutores-activos")
    public Map<?, Long> tutoresActivos() {
        return estadisticaService.tutoresActivos();
    }

    // 5️⃣ Alumnos activos
    @GetMapping("/alumnos-activos")
    public Map<?, Long> alumnosActivos() {
        return estadisticaService.alumnosActivos();
    }
}

