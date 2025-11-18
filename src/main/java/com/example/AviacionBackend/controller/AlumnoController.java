package com.example.AviacionBackend.controller;

import com.example.AviacionBackend.model.Usuario;
import com.example.AviacionBackend.model.Vuelo;
import com.example.AviacionBackend.model.dto.UsuarioDTO;
import com.example.AviacionBackend.model.dto.VueloDTO;
import com.example.AviacionBackend.service.AlumnoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/alumnos")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AlumnoController {

    private final AlumnoService alumnoService;

    /** 🔹 Obtener perfil del alumno */
    @GetMapping("/{id}/perfil")
    public UsuarioDTO obtenerPerfil(@PathVariable Long id) {
        return alumnoService.obtenerPerfil(id);
    }

    /** 🔹 Obtener todos los vuelos del alumno */
    @GetMapping("/{id}/vuelos")
    public List<VueloDTO> obtenerVuelos(@PathVariable Long id) {
        return alumnoService.obtenerVuelos(id);
    }

    /** 🔹 Obtener próximos vuelos */
    @GetMapping("/{id}/vuelos/proximos")
    public List<VueloDTO> obtenerProximosVuelos(@PathVariable Long id) {
        return alumnoService.obtenerProximosVuelos(id);
    }

    /** 🔹 Obtener un vuelo específico */
    @GetMapping("/{id}/vuelos/{idVuelo}")
    public VueloDTO obtenerVueloEspecifico(
            @PathVariable Long id,
            @PathVariable Long idVuelo
    ) {
        return alumnoService.obtenerVueloEspecifico(id, idVuelo);
    }

    /** 🔹 Obtener estadísticas globales (para RADAR) */
    @GetMapping("/{id}/estadisticas")
    public Map<String, Object> obtenerEstadisticas(
            @PathVariable Long id
    ) {
        return alumnoService.obtenerEstadisticas(id);
    }
}
