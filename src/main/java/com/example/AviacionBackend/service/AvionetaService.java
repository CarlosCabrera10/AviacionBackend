package com.example.AviacionBackend.service;

import com.example.AviacionBackend.model.Avioneta;
import com.example.AviacionBackend.repository.AvionetaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvionetaService {

    private final AvionetaRepository avionetaRepository;

    public List<Avioneta> listar() {
        return avionetaRepository.findAll();
    }

    public Avioneta obtenerPorId(Long id) {
        return avionetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avioneta no encontrada"));
    }

    public Avioneta guardar(Avioneta avioneta) {
        return avionetaRepository.save(avioneta);
    }

    public Avioneta actualizar(Long id, Avioneta datos) {
        return avionetaRepository.findById(id)
                .map(a -> {
                    a.setCodigo(datos.getCodigo());
                    a.setModelo(datos.getModelo());
                    a.setHorasVuelo(datos.getHorasVuelo());
                    a.setEstado(datos.getEstado());
                    return avionetaRepository.save(a);
                })
                .orElseThrow(() -> new RuntimeException("Avioneta no encontrada"));
    }

    public void eliminar(Long id) {
        avionetaRepository.deleteById(id);
    }

    public Avioneta desactivar(Long id) {
        Avioneta av = avionetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe la avioneta"));

        av.setEstado(Avioneta.EstadoAvioneta.Desactivada);
        return avionetaRepository.save(av);  // devuelve el objeto actualizado
    }


}
