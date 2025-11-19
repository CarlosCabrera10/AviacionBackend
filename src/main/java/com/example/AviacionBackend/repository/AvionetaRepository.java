package com.example.AviacionBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.AviacionBackend.model.Avioneta;

@Repository
public interface AvionetaRepository extends JpaRepository<Avioneta, Long> {


}
