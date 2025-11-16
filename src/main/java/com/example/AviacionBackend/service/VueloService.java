package com.example.AviacionBackend.service;

import com.example.AviacionBackend.model.Avioneta;
import com.example.AviacionBackend.model.Usuario;
import com.example.AviacionBackend.model.Vuelo;
import com.example.AviacionBackend.model.EspacioVuelo;
import com.example.AviacionBackend.model.dto.VueloDTO;
import com.example.AviacionBackend.repository.AvionetaRepository;
import com.example.AviacionBackend.repository.UsuarioRepository;
import com.example.AviacionBackend.repository.VueloRepository;
import com.example.AviacionBackend.repository.EspacioVueloRepository;
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
    private final EspacioVueloRepository espacioVueloRepository;

    // -----------------------------
    //        MÉTODOS USANDO DTO
    // -----------------------------

    public List<VueloDTO> listar() {
        return vueloRepository.findAll()
                .stream()
                .map(this::convertirA_DTO)
                .collect(Collectors.toList());
    }

    public VueloDTO obtenerPorId(Long id) {
        Vuelo vuelo = vueloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vuelo no encontrado"));

        return convertirA_DTO(vuelo);
    }

    public VueloDTO guardar(VueloDTO dto) {

        Vuelo vuelo = new Vuelo();

        vuelo.setFecha(dto.getFecha());
        vuelo.setHoraInicio(dto.getHoraInicio());
        vuelo.setHoraFin(dto.getHoraFin());
        vuelo.setEstado(dto.getEstado());
        vuelo.setObservacion(dto.getObservacion());

        Usuario alumno = usuarioRepository.findById(dto.getIdAlumno())
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));

        Usuario tutor = usuarioRepository.findById(dto.getIdTutor())
                .orElseThrow(() -> new RuntimeException("Tutor no encontrado"));

        Avioneta avioneta = avionetaRepository.findById(dto.getIdAvioneta())
                .orElseThrow(() -> new RuntimeException("Avioneta no encontrada"));

        vuelo.setAlumno(alumno);
        vuelo.setTutor(tutor);
        vuelo.setAvioneta(avioneta);

        Vuelo guardado = vueloRepository.save(vuelo);

        return convertirA_DTO(guardado);
    }

    public VueloDTO actualizar(Long id, VueloDTO dto) {
        Vuelo vuelo = vueloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vuelo no encontrado"));

        if (dto.getFecha() != null) vuelo.setFecha(dto.getFecha());
        if (dto.getHoraInicio() != null) vuelo.setHoraInicio(dto.getHoraInicio());
        if (dto.getHoraFin() != null) vuelo.setHoraFin(dto.getHoraFin());
        if (dto.getEstado() != null) vuelo.setEstado(dto.getEstado());
        if (dto.getObservacion() != null) vuelo.setObservacion(dto.getObservacion());

        if (dto.getIdAlumno() != null) {
            Usuario alumno = usuarioRepository.findById(dto.getIdAlumno())
                    .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));
            vuelo.setAlumno(alumno);
        }

        if (dto.getIdTutor() != null) {
            Usuario tutor = usuarioRepository.findById(dto.getIdTutor())
                    .orElseThrow(() -> new RuntimeException("Tutor no encontrado"));
            vuelo.setTutor(tutor);
        }

        if (dto.getIdAvioneta() != null) {
            Avioneta avioneta = avionetaRepository.findById(dto.getIdAvioneta())
                    .orElseThrow(() -> new RuntimeException("Avioneta no encontrada"));
            vuelo.setAvioneta(avioneta);
        }

        Vuelo actualizado = vueloRepository.save(vuelo);

        return convertirA_DTO(actualizado);
    }

    public List<VueloDTO> listarPorTutor(Long idTutor) {
        return vueloRepository.findByTutor_id(idTutor)
                .stream()
                .map(this::convertirA_DTO)
                .collect(Collectors.toList());
    }

    public void eliminar(Long id) {
        vueloRepository.deleteById(id);
    }

    // -----------------------------
    //            MAPPER
    // -----------------------------
    private VueloDTO convertirA_DTO(Vuelo v) {

        VueloDTO dto = new VueloDTO();

        dto.setIdVuelo(v.getIdVuelo());
        dto.setFecha(v.getFecha());
        dto.setHoraInicio(v.getHoraInicio());
        dto.setHoraFin(v.getHoraFin());
        dto.setEstado(v.getEstado());
        dto.setObservacion(v.getObservacion());

        if (v.getAlumno() != null) {
            dto.setIdAlumno(v.getAlumno().getId());
            dto.setNombreAlumno(v.getAlumno().getNombre());
        }

        if (v.getTutor() != null) {
            dto.setIdTutor(v.getTutor().getId());
            dto.setNombreTutor(v.getTutor().getNombre());
        }

        if (v.getAvioneta() != null) {
            dto.setIdAvioneta(v.getAvioneta().getIdAvioneta());
            dto.setCodigoAvioneta(v.getAvioneta().getCodigo());
        }

        return dto;
    }
}
