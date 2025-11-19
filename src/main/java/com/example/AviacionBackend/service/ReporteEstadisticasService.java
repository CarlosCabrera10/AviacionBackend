package com.example.AviacionBackend.service;

import com.example.AviacionBackend.model.Vuelo;
import com.example.AviacionBackend.repository.VueloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteEstadisticasService {

    private final VueloRepository vueloRepository;

    // Obtener vuelos válidos
    private List<Vuelo> obtenerVuelosValidos() {
        return vueloRepository
                .findByEstadoNotAndAlumno_ActivoTrueAndTutor_ActivoTrue(Vuelo.Estado.Cancelado);
    }

    // 1️⃣ Actividad por día
    public Map<LocalDate, Long> actividadPorDia() {
        return obtenerVuelosValidos().stream()
                .collect(Collectors.groupingBy(
                        Vuelo::getFecha,
                        Collectors.counting()
                ));
    }

    // 2️⃣ Actividad por hora (usamos horaInicio)
    public Map<Integer, Long> actividadPorHora() {
        return obtenerVuelosValidos().stream()
                .collect(Collectors.groupingBy(
                        v -> v.getHoraInicio().getHour(),
                        Collectors.counting()
                ));
    }

    // 3️⃣ Uso de avionetas
    public Map<String, Long> usoAvionetas() {
        return obtenerVuelosValidos().stream()
                .collect(Collectors.groupingBy(
                        v -> v.getAvioneta().getModelo(),
                        Collectors.counting()
                ));
    }

    // 4️⃣ Actividad de tutores
    public Map<String, Long> tutoresActivos() {
        return obtenerVuelosValidos().stream()
                .collect(Collectors.groupingBy(
                        v -> v.getTutor().getNombre() + " " + v.getTutor().getApellido(),
                        Collectors.counting()
                ));
    }

    // 5️⃣ Actividad de alumnos
    public Map<String, Long> alumnosActivos() {
        return obtenerVuelosValidos().stream()
                .collect(Collectors.groupingBy(
                        v -> v.getAlumno().getNombre() + " " + v.getAlumno().getApellido(),
                        Collectors.counting()
                ));
    }

    public Map<String, Double> horasVueloPorAvioneta() {
        return vueloRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        v -> v.getAvioneta().getModelo(),
                        Collectors.summingDouble(
                                v -> {
                                    if (v.getHoraInicio() != null && v.getHoraFin() != null) {
                                        return java.time.Duration.between(
                                                v.getHoraInicio(),
                                                v.getHoraFin()
                                        ).toMinutes() / 60.0;
                                    }
                                    return 0.0;
                                }
                        )
                ));
    }



    public Map<String, Long> vuelosPorTutor() {
        return obtenerVuelosValidos().stream()
                .collect(Collectors.groupingBy(
                        v -> v.getTutor().getNombre() + " " + v.getTutor().getApellido(),
                        Collectors.counting()
                ));
    }

    public Map<String, Map<Integer, Integer>> mapaCalorHorarios(LocalDate fechaInicio, LocalDate fechaFin) {

        List<Vuelo> vuelos = vueloRepository.findByFechaBetweenAndEstadoIn(
                fechaInicio,
                fechaFin,
                List.of(Vuelo.Estado.Programado, Vuelo.Estado.Completado)
        );

        Map<String, Map<Integer, Integer>> resultado = new LinkedHashMap<>();

        DateTimeFormatter diaFormatter =
                DateTimeFormatter.ofPattern("EEEE", new Locale("es", "ES"));

        // Inicializamos días y horas vacías (0-23)
        List<String> diasSemana = List.of(
                "lunes", "martes", "miércoles", "jueves",
                "viernes", "sábado", "domingo"
        );

        for (String dia : diasSemana) {
            Map<Integer, Integer> horas = new LinkedHashMap<>();
            for (int h = 0; h < 24; h++) {
                horas.put(h, 0);
            }
            resultado.put(dia, horas);
        }

        // Rellenamos con los vuelos reales
        for (Vuelo v : vuelos) {

            // 📌 Día en texto (lunes, martes, …)
            String dia = v.getFecha().format(diaFormatter).toLowerCase();

            // 📌 Hora (usamos horaInicio)
            int hora = v.getHoraInicio().getHour();

            // Incrementar contador
            resultado.get(dia).put(hora, resultado.get(dia).get(hora) + 1);
        }

        return resultado;
    }



}
