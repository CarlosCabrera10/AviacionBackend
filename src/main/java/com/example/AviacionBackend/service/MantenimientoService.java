package com.example.AviacionBackend.service;

import com.example.AviacionBackend.model.Avioneta;
import com.example.AviacionBackend.model.Mantenimiento;
import com.example.AviacionBackend.repository.AvionetaRepository;
import com.example.AviacionBackend.repository.MantenimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        // 🔥 Cuando se crea un mantenimiento → pasar avioneta a Mantenimiento
        avioneta.setEstado(Avioneta.EstadoAvioneta.Mantenimiento);
        avionetaRepository.save(avioneta);

        datos.setAvioneta(avioneta);
        return mantenimientoRepository.save(datos);
    }

    public Mantenimiento actualizar(Long id, Mantenimiento datos) {
        return mantenimientoRepository.findById(id).map(m -> {

            if (datos.getDescripcion() != null) m.setDescripcion(datos.getDescripcion());
            if (datos.getTipo() != null) m.setTipo(datos.getTipo());
            if (datos.getEstado() != null) {

                m.setEstado(datos.getEstado());

                // 🔥 Si el mantenimiento se marca como FINALIZADO → avioneta vuelve a ACTIVE
                if (datos.getEstado().equals("Finalizado")) {
                    Avioneta avioneta = m.getAvioneta();
                    avioneta.setEstado(Avioneta.EstadoAvioneta.Activo);
                    avionetaRepository.save(avioneta);
                }
            }

            if (datos.getNotas() != null) m.setNotas(datos.getNotas());
            if (datos.getFechaFin() != null) m.setFechaFin(datos.getFechaFin());

            if (datos.getAvioneta() != null) {
                Avioneta avioneta = avionetaRepository.findById(datos.getAvioneta().getIdAvioneta())
                        .orElseThrow(() -> new RuntimeException("Avioneta no encontrada"));
                m.setAvioneta(avioneta);
            }

            return mantenimientoRepository.save(m);

        }).orElseThrow(() -> new RuntimeException("Mantenimiento no encontrado"));
    }

    public void eliminar(Long id) {
        mantenimientoRepository.deleteById(id);
    }

    public List<Mantenimiento> listarPorAvioneta(Long idAvioneta) {
        return mantenimientoRepository.findByAvioneta_IdAvioneta(idAvioneta);
    }
}
