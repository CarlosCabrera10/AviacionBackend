package com.example.AviacionBackend.service;

import com.example.AviacionBackend.model.Avioneta;
import com.example.AviacionBackend.model.Mantenimiento;
import com.example.AviacionBackend.repository.AvionetaRepository;
import com.example.AviacionBackend.repository.MantenimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MantenimientoService {

    private final MantenimientoRepository mantenimientoRepository;
    private final AvionetaRepository avionetaRepository;

    public List<Mantenimiento> listar() {
        return mantenimientoRepository.findAll();
    }

    public Mantenimiento obtenerPorId(Long id) {
        return mantenimientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mantenimiento no encontrado"));
    }

    public Mantenimiento guardar(Mantenimiento datos) {

        Avioneta avioneta = avionetaRepository.findById(datos.getAvioneta().getIdAvioneta())
                .orElseThrow(() -> new RuntimeException("Avioneta no encontrada"));

        // Al crear un mantenimiento, poner avioneta en estado Mantenimiento
        avioneta.setEstado(Avioneta.EstadoAvioneta.Mantenimiento);
        avionetaRepository.save(avioneta);

        // Forzar fechas al crear
        datos.setFechaInicio(LocalDateTime.now());
        datos.setFechaFin(null);
        datos.setAvioneta(avioneta);

        return mantenimientoRepository.save(datos);
    }

    public Mantenimiento actualizar(Long id, Mantenimiento datos) {
        return mantenimientoRepository.findById(id).map(m -> {

            if (datos.getDescripcion() != null) m.setDescripcion(datos.getDescripcion());
            if (datos.getTipo() != null) m.setTipo(datos.getTipo());
            if (datos.getNotas() != null) m.setNotas(datos.getNotas());

            // Actualizar estado y fechas
            if (datos.getEstado() != null) {
                m.setEstado(datos.getEstado());
                Avioneta avioneta = m.getAvioneta();

                if (datos.getEstado() == Mantenimiento.Estado.Finalizado) {
                    avioneta.setEstado(Avioneta.EstadoAvioneta.Activo);
                    if (m.getFechaFin() == null) {
                        m.setFechaFin(LocalDateTime.now());
                    }
                } else if (datos.getEstado() == Mantenimiento.Estado.En_proceso) {
                    avioneta.setEstado(Avioneta.EstadoAvioneta.Mantenimiento);
                    m.setFechaInicio(LocalDateTime.now());
                    m.setFechaFin(null);
                }

                avionetaRepository.save(avioneta);
            }

            // Actualizar la avioneta si se cambia
            if (datos.getAvioneta() != null) {
                Avioneta avioneta = avionetaRepository.findById(datos.getAvioneta().getIdAvioneta())
                        .orElseThrow(() -> new RuntimeException("Avioneta no encontrada"));
                m.setAvioneta(avioneta);
            }

            return mantenimientoRepository.save(m);

        }).orElseThrow(() -> new RuntimeException("Mantenimiento no encontrado"));
    }

    public List<Mantenimiento> listarPorAvioneta(Long idAvioneta) {
        return mantenimientoRepository.findByAvioneta_IdAvioneta(idAvioneta);
    }
}
