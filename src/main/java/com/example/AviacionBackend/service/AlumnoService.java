package com.example.AviacionBackend.service;

import com.example.AviacionBackend.model.Usuario;
import com.example.AviacionBackend.model.Vuelo;
import com.example.AviacionBackend.model.dto.UsuarioDTO;
import com.example.AviacionBackend.model.dto.VueloDTO;
import com.example.AviacionBackend.repository.RendimientoAlumnoRepository;
import com.example.AviacionBackend.repository.UsuarioRepository;
import com.example.AviacionBackend.repository.VueloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlumnoService {

    private final UsuarioRepository usuarioRepository;
    private final VueloRepository vueloRepository;
    private final RendimientoAlumnoRepository rendimientoRepo;
    private final VueloService vueloService; // para mapear a DTO

    /** 🔹 Perfil */
    public UsuarioDTO obtenerPerfil(Long idAlumno) {
        Usuario alumno = usuarioRepository.findById(idAlumno)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));

        UsuarioDTO dto = new UsuarioDTO();

        dto.setId(alumno.getId());
        dto.setNombre(alumno.getNombre());
        dto.setApellido(alumno.getApellido());
        dto.setCorreo(alumno.getCorreo());
        dto.setTelefono(alumno.getTelefono());
        dto.setRol(alumno.getRol().name());
        dto.setActivo(alumno.getActivo());

        return dto;
    }

    /** 🔹 Todos los vuelos del alumno */
    public List<VueloDTO> obtenerVuelos(Long idAlumno) {
        return vueloRepository.findByAlumno_Id(idAlumno)
                .stream()
                .map(this::mapVuelo) // usamos tu mismo mapper
                .sorted(Comparator.comparing(VueloDTO::getFecha).reversed())
                .collect(Collectors.toList());
    }

    /** 🔹 Próximos vuelos */
    public List<VueloDTO> obtenerProximosVuelos(Long idAlumno) {
        return vueloRepository.findByAlumno_Id(idAlumno)
                .stream()
                .filter(v -> v.getEstado() == Vuelo.Estado.Programado
                        && v.getFecha() != null
                        && !v.getFecha().isBefore(java.time.LocalDate.now()))
                .map(this::mapVuelo)
                .sorted(Comparator.comparing(VueloDTO::getFecha))
                .collect(Collectors.toList());
    }

    /** 🔹 Obtener un vuelo específico */
    public VueloDTO obtenerVueloEspecifico(Long idAlumno, Long idVuelo) {
        Vuelo vuelo = vueloRepository.findById(idVuelo)
                .orElseThrow(() -> new RuntimeException("Vuelo no encontrado"));

        if (!Objects.equals(vuelo.getAlumno().getId(), idAlumno)) {
            throw new RuntimeException("Este vuelo no pertenece al alumno");
        }

        return mapVuelo(vuelo); // ← SOLUCIÓN
    }

    /** 🔹 Estadísticas para radar (1–10) */
    public Map<String, Object> obtenerEstadisticas(Long idAlumno) {

        var rendimientos = rendimientoRepo.findByVuelo_Alumno_Id(idAlumno);

        if (rendimientos.isEmpty()) {
            return Map.of(
                    "tecnicaAterrizaje", 0,
                    "maniobras", 0,
                    "comunicacionRadio", 0,
                    "seguimientoInstrucciones", 0,
                    "puntualidad", 0,
                    "comportamiento", 0
            );
        }

        double avgTecnica = rendimientos.stream()
                .filter(r -> r.getTecnicaAterrizaje() != null)
                .mapToDouble(r -> r.getTecnicaAterrizaje().doubleValue()).average().orElse(0);

        double avgManiobras = rendimientos.stream()
                .filter(r -> r.getManiobras() != null)
                .mapToDouble(r -> r.getManiobras().doubleValue()).average().orElse(0);

        double avgRadio = rendimientos.stream()
                .filter(r -> r.getComunicacionRadio() != null)
                .mapToDouble(r -> r.getComunicacionRadio().doubleValue()).average().orElse(0);

        double avgInst = rendimientos.stream()
                .filter(r -> r.getSeguimientoInstrucciones() != null)
                .mapToDouble(r -> r.getSeguimientoInstrucciones().doubleValue()).average().orElse(0);

        double avgPunt = rendimientos.stream()
                .filter(r -> r.getPuntualidad() != null)
                .mapToDouble(r -> r.getPuntualidad().doubleValue()).average().orElse(0);

        double avgComp = rendimientos.stream()
                .filter(r -> r.getComportamiento() != null)
                .mapToDouble(r -> r.getComportamiento().doubleValue()).average().orElse(0);

        return Map.of(
                "tecnicaAterrizaje", avgTecnica,
                "maniobras", avgManiobras,
                "comunicacionRadio", avgRadio,
                "seguimientoInstrucciones", avgInst,
                "puntualidad", avgPunt,
                "comportamiento", avgComp
        );
    }

    private VueloDTO mapVuelo(Vuelo v) {
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