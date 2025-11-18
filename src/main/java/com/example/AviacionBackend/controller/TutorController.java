package com.example.AviacionBackend.controller;


import com.example.AviacionBackend.model.dto.RendimientoAlumnoDTO;
import com.example.AviacionBackend.model.dto.UsuarioDTO;
import com.example.AviacionBackend.model.dto.VueloDTO;
import com.example.AviacionBackend.service.TutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tutor")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TutorController {

    private final TutorService tutorService;

    // 🔹 1. Obtener alumnos únicos del tutor
    @GetMapping("/{idTutor}/alumnos")
    public List<UsuarioDTO> obtenerAlumnos(@PathVariable Long idTutor) {
        return tutorService.obtenerAlumnosPorTutor(idTutor);
    }

    // 🔹 2. Obtener vuelos por alumno y tutor (por si lo usas en otro lado)
    @GetMapping("/{idTutor}/alumno/{idAlumno}/vuelos")
    public List<VueloDTO> obtenerVuelosPorAlumno(
            @PathVariable Long idTutor,
            @PathVariable Long idAlumno) {
        return tutorService.obtenerVuelosPorAlumno(idTutor, idAlumno);
    }

    // 🔹 3. Estadísticas globales del alumno (promedios para radar chart)
    @GetMapping("/alumno/{idAlumno}/estadisticas")
    public RendimientoAlumnoDTO obtenerEstadisticas(@PathVariable Long idAlumno) {
        return tutorService.obtenerEstadisticasAlumno(idAlumno);
    }

    // ⭐ 4. NUEVO → Datos del alumno (para el encabezado de detalle-alumno)
    @GetMapping("/alumno/{idAlumno}")
    public UsuarioDTO obtenerAlumno(@PathVariable Long idAlumno) {
        return tutorService.obtenerAlumnoPorId(idAlumno);
    }

    // ⭐ 5. NUEVO → Vuelos del alumno (solo por idAlumno, para detalle-alumno)
    @GetMapping("/alumno/{idAlumno}/vuelos")
    public List<VueloDTO> obtenerVuelosAlumno(@PathVariable Long idAlumno) {
        return tutorService.obtenerVuelosPorAlumnoSoloAlumno(idAlumno);
    }
}