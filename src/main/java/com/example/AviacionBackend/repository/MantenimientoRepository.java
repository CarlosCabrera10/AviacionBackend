package com.example.AviacionBackend.repository;

import com.example.AviacionBackend.model.Mantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MantenimientoRepository extends JpaRepository<Mantenimiento, Long> {

    List<Mantenimiento> findByAvioneta_IdAvioneta(Long idAvioneta);

}
