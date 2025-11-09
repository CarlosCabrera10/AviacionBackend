package com.example.AviacionBackend.repository;

import com.example.AviacionBackend.model.Vuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VueloRepository extends JpaRepository<Vuelo, Long> {
    List<Vuelo> findByTutor_id(Long tutorId);
}
