package com.example.AviacionBackend.service;


import com.example.AviacionBackend.model.Avioneta;
import com.example.AviacionBackend.repository.AvionetaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AvionetaService {

    private final AvionetaRepository avionetaRepository;

    public AvionetaService(AvionetaRepository avionetaRepository) {
        this.avionetaRepository = avionetaRepository;
    }

    public List<Avioneta> obtenerTodas() {
        return avionetaRepository.findAll();
    }

    public Optional<Avioneta> obtenerPorId(Long id) {
        return avionetaRepository.findById(id);
    }

    public Avioneta guardar(Avioneta avioneta) {
        return avionetaRepository.save(avioneta);
    }

    public void eliminar(Long id) {
        avionetaRepository.deleteById(id);
    }
}
