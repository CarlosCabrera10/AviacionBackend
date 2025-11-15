package com.example.AviacionBackend.service;

import com.example.AviacionBackend.model.Vuelo;
import com.example.AviacionBackend.repository.VueloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteEstadisticasService {

    private final VueloRepository vueloRepository;

    // Método para obtener solo vuelos válidos (no cancelados y usuarios activos)
    private List<Vuelo> obtenerVuelosValidos() {
        return vueloRepository
                .findByEstadoNotAndAlumno_ActivoTrueAndTutor_ActivoTrue(Vuelo.Estado.Cancelado);
    }

    // 1️⃣ Actividad por día (para heatmap)
    public Map<LocalDate, Long> actividadPorDia() {
        List<Vuelo> vuelos = obtenerVuelosValidos();

        return vuelos.stream()
                .collect(Collectors.groupingBy(
                        Vuelo::getFecha,
                        Collectors.counting()
                ));
    }

    // 2️⃣ Actividad por hora
    public Map<Integer, Long> actividadPorHora() {
        List<Vuelo> vuelos = obtenerVuelosValidos();

        return vuelos.stream()
                .collect(Collectors.groupingBy(
                        v -> v.getHora().getHour(),
                        Collectors.counting()
                ));
    }

    // 3️⃣ Uso de avionetas
    public Map<String, Long> usoAvionetas() {
        List<Vuelo> vuelos = obtenerVuelosValidos();

        return vuelos.stream()
                .collect(Collectors.groupingBy(
                        v -> v.getAvioneta().getModelo(),
                        Collectors.counting()
                ));
    }

    // 4️⃣ Actividad de tutores
    public Map<String, Long> tutoresActivos() {
        List<Vuelo> vuelos = obtenerVuelosValidos();

        return vuelos.stream()
                .collect(Collectors.groupingBy(
                        v -> v.getTutor().getNombre() + " " + v.getTutor().getApellido(),
                        Collectors.counting()
                ));
    }

    // 5️⃣ Actividad de alumnos
    public Map<String, Long> alumnosActivos() {
        List<Vuelo> vuelos = obtenerVuelosValidos();

        return vuelos.stream()
                .collect(Collectors.groupingBy(
                        v -> v.getAlumno().getNombre() + " " + v.getAlumno().getApellido(),
                        Collectors.counting()
                ));
    }
}
