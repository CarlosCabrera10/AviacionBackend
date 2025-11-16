package com.example.AviacionBackend.service;

import com.example.AviacionBackend.model.EspacioVuelo;
import com.example.AviacionBackend.repository.EspacioVueloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EspacioVueloService {

    private final EspacioVueloRepository espacioVueloRepository;

    public List<EspacioVuelo> listar() {
        return espacioVueloRepository.findAll();
    }

    public EspacioVuelo obtenerPorId(Long id) {
        return espacioVueloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Espacio de vuelo no encontrado"));
    }

    public EspacioVuelo guardar(EspacioVuelo espacioVuelo) {
        return espacioVueloRepository.save(espacioVuelo);
    }

    public EspacioVuelo actualizar(Long id, EspacioVuelo datos) {
        return espacioVueloRepository.findById(id)
                .map(e -> {
                    e.setNombre(datos.getNombre());
                    e.setDescripcion(datos.getDescripcion());
                    return espacioVueloRepository.save(e);
                })
                .orElseThrow(() -> new RuntimeException("Espacio de vuelo no encontrado"));
    }

    public void eliminar(Long id) {
        espacioVueloRepository.deleteById(id);
    }
}
