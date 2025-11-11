package com.example.AviacionBackend.service;

import com.example.AviacionBackend.model.dto.VueloDTO;
import com.example.AviacionBackend.model.Avioneta;
import com.example.AviacionBackend.model.Usuario;
import com.example.AviacionBackend.model.Vuelo;
import com.example.AviacionBackend.repository.AvionetaRepository;
import com.example.AviacionBackend.repository.UsuarioRepository;
import com.example.AviacionBackend.repository.VueloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VueloService {

    private final VueloRepository vueloRepository;
    private final UsuarioRepository usuarioRepository;
    private final AvionetaRepository avionetaRepository;

    // Convertir entidad a DTO
    public VueloDTO convertirADTO(Vuelo vuelo) {
        VueloDTO dto = new VueloDTO();
        dto.setIdVuelo(vuelo.getIdVuelo());
        dto.setIdAlumno(vuelo.getAlumno().getId());
        dto.setIdTutor(vuelo.getTutor().getId());
        dto.setIdAvioneta(vuelo.getAvioneta().getIdAvioneta());

        dto.setNombreAlumno(vuelo.getAlumno().getNombre() + " " + vuelo.getAlumno().getApellido());
        dto.setNombreTutor(vuelo.getTutor().getNombre() + " " + vuelo.getTutor().getApellido());
        dto.setCodigoAvioneta(vuelo.getAvioneta().getCodigo());

        dto.setFecha(vuelo.getFecha());
        dto.setHora(vuelo.getHora());
        dto.setEstado(vuelo.getEstado());
        dto.setObservacion(vuelo.getObservacion());
        return dto;
    }

    // Listar todos los vuelos como DTO
    public List<VueloDTO> listar() {
        return vueloRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener un vuelo por ID como DTO
    public VueloDTO obtenerPorId(Long id) {
        return vueloRepository.findById(id)
                .map(this::convertirADTO)
                .orElseThrow(() -> new RuntimeException("Vuelo no encontrado"));
    }

    // Guardar un vuelo usando DTO
    public VueloDTO guardar(VueloDTO dto) {
        Vuelo vuelo = new Vuelo();

        Usuario alumno = usuarioRepository.findById(dto.getIdAlumno())
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));
        Usuario tutor = usuarioRepository.findById(dto.getIdTutor())
                .orElseThrow(() -> new RuntimeException("Tutor no encontrado"));
        Avioneta avioneta = avionetaRepository.findById(dto.getIdAvioneta())
                .orElseThrow(() -> new RuntimeException("Avioneta no encontrada"));

        vuelo.setAlumno(alumno);
        vuelo.setTutor(tutor);
        vuelo.setAvioneta(avioneta);
        vuelo.setFecha(dto.getFecha());
        vuelo.setHora(dto.getHora());
        vuelo.setEstado(dto.getEstado() != null ? dto.getEstado() : Vuelo.Estado.Programado);
        vuelo.setObservacion(dto.getObservacion());

        Vuelo guardado = vueloRepository.save(vuelo);
        return convertirADTO(guardado);
    }

    // Actualizar un vuelo existente usando DTO
    public VueloDTO actualizar(Long id, VueloDTO dto) {
        Vuelo vueloExistente = vueloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vuelo no encontrado"));

        Usuario alumno = usuarioRepository.findById(dto.getIdAlumno())
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));
        Usuario tutor = usuarioRepository.findById(dto.getIdTutor())
                .orElseThrow(() -> new RuntimeException("Tutor no encontrado"));
        Avioneta avioneta = avionetaRepository.findById(dto.getIdAvioneta())
                .orElseThrow(() -> new RuntimeException("Avioneta no encontrada"));

        vueloExistente.setAlumno(alumno);
        vueloExistente.setTutor(tutor);
        vueloExistente.setAvioneta(avioneta);
        vueloExistente.setFecha(dto.getFecha());
        vueloExistente.setHora(dto.getHora());
        vueloExistente.setEstado(dto.getEstado() != null ? dto.getEstado() : Vuelo.Estado.Programado);
        vueloExistente.setObservacion(dto.getObservacion());

        Vuelo actualizado = vueloRepository.save(vueloExistente);
        return convertirADTO(actualizado);
    }

    // Eliminar vuelo
    public void eliminar(Long id) {
        vueloRepository.deleteById(id);
    }

    // Listar vuelos por tutor
    public List<VueloDTO> listarPorTutor(Long idTutor) {
        return vueloRepository.findByTutor_id(idTutor)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
}
