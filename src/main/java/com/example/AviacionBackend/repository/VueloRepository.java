package com.example.AviacionBackend.repository;

import com.example.AviacionBackend.model.Vuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VueloRepository extends JpaRepository<Vuelo, Long> {
    List<Vuelo> findByTutor_id(Long tutorId);
    List<Vuelo> findByEstadoNotAndAlumno_ActivoTrueAndTutor_ActivoTrue(Vuelo.Estado estado);
    List<Vuelo> findByTutor_idAndAlumno_id(Long tutorId, Long alumnoId);
    List<Vuelo> findByAlumno_Id(Long idAlumno);




}
