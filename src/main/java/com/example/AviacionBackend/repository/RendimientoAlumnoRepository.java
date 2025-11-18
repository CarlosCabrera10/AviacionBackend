package com.example.AviacionBackend.repository;

import com.example.AviacionBackend.model.RendimientoAlumno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RendimientoAlumnoRepository extends JpaRepository<RendimientoAlumno, Long> {

    // Un rendimiento por vuelo (lógico), lo tratamos como único
    Optional<RendimientoAlumno> findByVuelo_IdVuelo(Long idVuelo);
    List<RendimientoAlumno> findByVuelo_Alumno_Id(Long idAlumno);

}