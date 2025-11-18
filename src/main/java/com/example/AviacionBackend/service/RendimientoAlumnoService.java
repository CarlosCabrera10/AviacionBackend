package com.example.AviacionBackend.service;

import com.example.AviacionBackend.model.RendimientoAlumno;
import com.example.AviacionBackend.model.Vuelo;
import com.example.AviacionBackend.model.dto.RendimientoAlumnoDTO;
import com.example.AviacionBackend.repository.RendimientoAlumnoRepository;
import com.example.AviacionBackend.repository.VueloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RendimientoAlumnoService {

    private final RendimientoAlumnoRepository rendimientoRepo;
    private final VueloRepository vueloRepository;

    public RendimientoAlumnoDTO obtenerPorVuelo(Long idVuelo) {
        return rendimientoRepo.findByVuelo_IdVuelo(idVuelo)
                .map(this::toDTO)
                .orElse(null); // luego si quieres lanzamos 404, pero de momento null = sin rendimiento
    }

    public RendimientoAlumnoDTO guardarOActualizar(Long idVuelo, RendimientoAlumnoDTO dto) {
        Vuelo vuelo = vueloRepository.findById(idVuelo)
                .orElseThrow(() -> new RuntimeException("Vuelo no encontrado"));

        // Si ya existe rendimiento para este vuelo, lo actualizamos
        RendimientoAlumno entidad = rendimientoRepo.findByVuelo_IdVuelo(idVuelo)
                .orElseGet(() -> {
                    RendimientoAlumno r = new RendimientoAlumno();
                    r.setVuelo(vuelo);
                    r.setFecha(LocalDateTime.now());
                    return r;
                });

        entidad.setTecnicaAterrizaje(dto.getTecnicaAterrizaje());
        entidad.setManiobras(dto.getManiobras());
        entidad.setComunicacionRadio(dto.getComunicacionRadio());
        entidad.setSeguimientoInstrucciones(dto.getSeguimientoInstrucciones());
        entidad.setPuntualidad(dto.getPuntualidad());
        entidad.setComportamiento(dto.getComportamiento());
        entidad.setComentarios(dto.getComentarios());

        // Calculamos el promedio (1-10) ignorando los null
        BigDecimal promedio = calcularPromedio(entidad);
        entidad.setPuntajeGeneral(promedio);

        if (entidad.getFecha() == null) {
            entidad.setFecha(LocalDateTime.now());
        }

        RendimientoAlumno guardado = rendimientoRepo.save(entidad);
        return toDTO(guardado);
    }

    private BigDecimal calcularPromedio(RendimientoAlumno r) {
        BigDecimal suma = BigDecimal.ZERO;
        int count = 0;

        if (r.getTecnicaAterrizaje() != null) {
            suma = suma.add(r.getTecnicaAterrizaje());
            count++;
        }
        if (r.getManiobras() != null) {
            suma = suma.add(r.getManiobras());
            count++;
        }
        if (r.getComunicacionRadio() != null) {
            suma = suma.add(r.getComunicacionRadio());
            count++;
        }
        if (r.getSeguimientoInstrucciones() != null) {
            suma = suma.add(r.getSeguimientoInstrucciones());
            count++;
        }
        if (r.getPuntualidad() != null) {
            suma = suma.add(r.getPuntualidad());
            count++;
        }
        if (r.getComportamiento() != null) {
            suma = suma.add(r.getComportamiento());
            count++;
        }

        if (count == 0) {
            return null; // sin datos, sin promedio
        }

        return suma
                .divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
    }

    private RendimientoAlumnoDTO toDTO(RendimientoAlumno r) {
        RendimientoAlumnoDTO dto = new RendimientoAlumnoDTO();
        dto.setIdRendimiento(r.getIdRendimiento());
        dto.setIdVuelo(r.getVuelo().getIdVuelo());

        dto.setPuntajeGeneral(r.getPuntajeGeneral());
        dto.setTecnicaAterrizaje(r.getTecnicaAterrizaje());
        dto.setManiobras(r.getManiobras());
        dto.setComunicacionRadio(r.getComunicacionRadio());
        dto.setSeguimientoInstrucciones(r.getSeguimientoInstrucciones());
        dto.setPuntualidad(r.getPuntualidad());
        dto.setComportamiento(r.getComportamiento());
        dto.setComentarios(r.getComentarios());
        dto.setFecha(r.getFecha());

        return dto;
    }
}