package com.example.AviacionBackend.repository;

import com.example.AviacionBackend.model.ReporteVuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReporteVueloRepository extends JpaRepository<ReporteVuelo, Long> {
    List<ReporteVuelo> findByVuelo_IdVuelo(Long idVuelo);
}

