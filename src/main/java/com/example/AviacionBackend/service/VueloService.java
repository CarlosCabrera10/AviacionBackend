package com.example.AviacionBackend.service;

import com.example.AviacionBackend.model.Vuelo;
import com.example.AviacionBackend.repository.VueloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VueloService {

    private final VueloRepository vueloRepository;

    public List<Vuelo> listar() {
        return vueloRepository.findAll();
    }

    public Vuelo obtenerPorId(Long id) {
        return vueloRepository.findById(id).orElse(null);
    }

    public Vuelo guardar(Vuelo vuelo) {
        return vueloRepository.save(vuelo);
    }

    public void eliminar(Long id) {
        vueloRepository.deleteById(id);
    }

    public List<Vuelo> listarPorTutor(Long idTutor) {
        return vueloRepository.findByTutor_id(idTutor);
    }
}
