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

    // Listar todos
    public List<EspacioVuelo> listar() {
        return espacioVueloRepository.findAll();
    }

    // Obtener por ID
    public EspacioVuelo obtenerPorId(Long id) {
        return espacioVueloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Espacio de vuelo no encontrado"));
    }

    // Guardar nuevo
    public EspacioVuelo guardar(EspacioVuelo espacioVuelo) {
        return espacioVueloRepository.save(espacioVuelo);
    }

    // Actualizar todos los campos
    public EspacioVuelo actualizar(Long id, EspacioVuelo datos) {
        return espacioVueloRepository.findById(id)
                .map(e -> {
                    e.setNombre(datos.getNombre());
                    e.setDescripcion(datos.getDescripcion());
                    e.setTipo(datos.getTipo());
                    e.setUbicacion(datos.getUbicacion());
                    e.setHabilitado(datos.getHabilitado() != null ? datos.getHabilitado() : e.getHabilitado());
                    return espacioVueloRepository.save(e);
                })
                .orElseThrow(() -> new RuntimeException("Espacio de vuelo no encontrado"));
    }

    // Desactivar (habilitado = false)
    public EspacioVuelo desactivar(Long id) {
        return espacioVueloRepository.findById(id)
                .map(e -> {
                    e.setHabilitado(false);
                    return espacioVueloRepository.save(e);
                })
                .orElseThrow(() -> new RuntimeException("Espacio de vuelo no encontrado"));
    }

    // Activar (habilitado = true)
    public EspacioVuelo activar(Long id) {
        return espacioVueloRepository.findById(id)
                .map(e -> {
                    e.setHabilitado(true);
                    return espacioVueloRepository.save(e);
                })
                .orElseThrow(() -> new RuntimeException("Espacio de vuelo no encontrado"));
    }

    // Eliminar si se quiere
    public void eliminar(Long id) {
        espacioVueloRepository.deleteById(id);
    }
}
