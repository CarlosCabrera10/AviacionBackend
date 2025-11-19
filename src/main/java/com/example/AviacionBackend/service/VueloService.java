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
    private final NotificacionService notificacionService;

    // -----------------------------
    // LISTAR
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

    // -----------------------------
    // GUARDAR
    // -----------------------------
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

        if (dto.getIdEspacioVuelo() != null) {
            EspacioVuelo espacio = espacioVueloRepository.findById(dto.getIdEspacioVuelo())
                    .orElseThrow(() -> new RuntimeException("Espacio de vuelo no encontrado"));
            vuelo.setEspacioVuelo(espacio);
        }

        // VALIDAR CHOQUES
        validarConflictos(vuelo);


        Vuelo guardado = vueloRepository.save(vuelo);

        notificacionService.crearNotificacion(
                dto.getIdAlumno(),
                "Se te asignó el vuelo #" + guardado.getIdVuelo() +
                        " programado para el día " + dto.getFecha()
        );

        return convertirA_DTO(guardado);
    }

    // -----------------------------
    // ACTUALIZAR
    // -----------------------------

    public VueloDTO actualizar(Long id, VueloDTO dto) {
        Vuelo vuelo = vueloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vuelo no encontrado"));

        // Estado original ANTES de actualizar
        Vuelo.Estado estadoOriginal = vuelo.getEstado();

        // Actualizar datos
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

        if (dto.getIdEspacioVuelo() != null) {
            EspacioVuelo espacio = espacioVueloRepository.findById(dto.getIdEspacioVuelo())
                    .orElseThrow(() -> new RuntimeException("Espacio de vuelo no encontrado"));
            vuelo.setEspacioVuelo(espacio);
        }

        validarConflictos(vuelo);

        // 🔥 Detectar cambio de estado
        if (dto.getEstado() != null && estadoOriginal != dto.getEstado()) {

            String msg = switch (dto.getEstado()) {
                case Programado -> "El vuelo #" + id + " ha sido programado nuevamente.";
                case Completado -> "El vuelo #" + id + " ha sido marcado como completado.";
                case Cancelado -> "El vuelo #" + id + " ha sido cancelado.";
            };

            notificacionService.crearNotificacion(
                    vuelo.getAlumno().getId(),
                    msg
            );
        }

        Vuelo actualizado = vueloRepository.save(vuelo);
        return convertirA_DTO(actualizado);
    }

    public void eliminar(Long id) {
        vueloRepository.deleteById(id);
    }

    public List<VueloDTO> listarPorTutor(Long idTutor) {
        return vueloRepository.findByTutor_id(idTutor)
                .stream()
                .map(this::convertirA_DTO)
                .collect(Collectors.toList());
    }


    // =============================
    // VALIDACIÓN DE CHOQUES
    // =============================
    private void validarConflictos(Vuelo nuevo) {

        List<Vuelo> existentes = vueloRepository
                .findByFechaAndHoraInicioLessThanAndHoraFinGreaterThan(
                        nuevo.getFecha(),
                        nuevo.getHoraFin(),
                        nuevo.getHoraInicio()
                );

        for (Vuelo v : existentes) {

            // Evitar comparar contra sí mismo en actualización
            if (nuevo.getIdVuelo() != null &&
                    nuevo.getIdVuelo().equals(v.getIdVuelo())) continue;

            // Alumno
            if (v.getAlumno().getId().equals(nuevo.getAlumno().getId())) {
                throw new RuntimeException("El alumno ya tiene un vuelo en ese horario.");
            }

            // Tutor
            if (v.getTutor().getId().equals(nuevo.getTutor().getId())) {
                throw new RuntimeException("El tutor ya tiene un vuelo en ese horario.");
            }

            // Avioneta
            if (v.getAvioneta().getIdAvioneta().equals(nuevo.getAvioneta().getIdAvioneta())) {
                throw new RuntimeException("La avioneta ya está asignada en ese horario.");
            }

            // Espacio de vuelo
            if (v.getEspacioVuelo() != null &&
                    nuevo.getEspacioVuelo() != null &&
                    v.getEspacioVuelo().getIdEspacioVuelo().equals(
                            nuevo.getEspacioVuelo().getIdEspacioVuelo()
                    )) {
                throw new RuntimeException("El espacio de vuelo ya está ocupado en ese horario.");
            }
        }
    }


    // -----------------------------
    // MAPPER DTO
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
            dto.setEstadoAvioneta(v.getAvioneta().getEstado().name());
        }

        if (v.getEspacioVuelo() != null) {
            dto.setIdEspacioVuelo(v.getEspacioVuelo().getIdEspacioVuelo());
            dto.setNombreEspacio(v.getEspacioVuelo().getNombre());
        }

        return dto;
    }
}
