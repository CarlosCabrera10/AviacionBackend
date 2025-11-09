package com.example.AviacionBackend.service;

import com.example.AviacionBackend.model.ReporteVuelo;
import com.example.AviacionBackend.repository.ReporteVueloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteVueloService {

    private final ReporteVueloRepository reporteRepository;

    public List<ReporteVuelo> listar() {
        return reporteRepository.findAll();
    }

    public ReporteVuelo obtenerPorId(Long id) {
        return reporteRepository.findById(id).orElse(null);
    }

    public ReporteVuelo guardar(ReporteVuelo reporte) {
        return reporteRepository.save(reporte);
    }

    public void eliminar(Long id) {
        reporteRepository.deleteById(id);
    }

    public List<ReporteVuelo> listarPorVuelo(Long idVuelo) {
        return reporteRepository.findByVuelo_IdVuelo(idVuelo);
    }
}
