package com.example.AviacionBackend.controller;

import com.example.AviacionBackend.service.ReporteEstadisticasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    // 4️⃣ Tutores activos (cantidad de vuelos)
    @GetMapping("/tutores-activos")
    public Map<?, Long> tutoresActivos() {
        return estadisticaService.tutoresActivos();
    }

    // 5️⃣ Alumnos activos (cantidad de vuelos)
    @GetMapping("/alumnos-activos")
    public Map<?, Long> alumnosActivos() {
        return estadisticaService.alumnosActivos();
    }

    // 6️⃣ NUEVO — Vuelos por tutor
    @GetMapping("/vuelos-por-tutor")
    public Map<?, Long> vuelosPorTutor() {
        return estadisticaService.vuelosPorTutor();
    }

    // 7️⃣ NUEVO — Horas de vuelo por avioneta
    @GetMapping("/horas-vuelo-avionetas")
    public Map<?, Double> horasVueloAvionetas() {
        return estadisticaService.horasVueloPorAvioneta();
    }


    // 9️⃣ Heatmap horarios
    @GetMapping("/heatmap-horarios")
    public ResponseEntity<?> heatmapHorarios(
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin
    ) {
        LocalDate ini = fechaInicio != null ? LocalDate.parse(fechaInicio) : LocalDate.now().minusMonths(1);
        LocalDate fin = fechaFin != null ? LocalDate.parse(fechaFin) : LocalDate.now();

        return ResponseEntity.ok(estadisticaService.mapaCalorHorarios(ini, fin));
    }

}
