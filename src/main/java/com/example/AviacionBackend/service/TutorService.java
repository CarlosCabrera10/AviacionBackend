package com.example.AviacionBackend.service;

import com.example.AviacionBackend.model.Usuario;
import com.example.AviacionBackend.model.Vuelo;
import com.example.AviacionBackend.model.dto.RendimientoAlumnoDTO;
import com.example.AviacionBackend.model.dto.UsuarioDTO;
import com.example.AviacionBackend.model.dto.VueloDTO;
import com.example.AviacionBackend.repository.RendimientoAlumnoRepository;
import com.example.AviacionBackend.repository.UsuarioRepository;
import com.example.AviacionBackend.repository.VueloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class TutorService {

    private final VueloRepository vueloRepo;
    private final UsuarioRepository usuarioRepo;
    private final RendimientoAlumnoRepository rendimientoRepo;

    // 🔹 1. Alumnos únicos por tutor
    public List<UsuarioDTO> obtenerAlumnosPorTutor(Long idTutor) {

        return vueloRepo.findByTutor_id(idTutor)
                .stream()
                .map(Vuelo::getAlumno)
                .distinct()
                .map(u -> new UsuarioDTO(
                        u.getId(),
                        u.getNombre(),
                        u.getApellido(),
                        u.getCorreo(),
                        u.getTelefono(),
                        u.getRol().name(),
                        u.getActivo()
                ))
                .collect(Collectors.toList());
    }

    // 🔹 2. Vuelos por alumno y tutor
    public List<VueloDTO> obtenerVuelosPorAlumno(Long idTutor, Long idAlumno) {
        return vueloRepo.findByTutor_idAndAlumno_id(idTutor, idAlumno)
                .stream()
                .map(this::mapVueloToDTO)
                .collect(Collectors.toList());
    }

    // ⭐ NUEVO: 4. Obtener datos de un alumno por id
    public UsuarioDTO obtenerAlumnoPorId(Long idAlumno) {
        Usuario u = usuarioRepo.findById(idAlumno)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));

        return new UsuarioDTO(
                u.getId(),
                u.getNombre(),
                u.getApellido(),
                u.getCorreo(),
                u.getTelefono(),
                u.getRol().name(),
                u.getActivo()
        );
    }

    // ⭐ NUEVO: 5. Vuelos del alumno (sin idTutor, para detalle-alumno)
    public List<VueloDTO> obtenerVuelosPorAlumnoSoloAlumno(Long idAlumno) {
        return vueloRepo.findByAlumno_Id(idAlumno)
                .stream()
                .map(this::mapVueloToDTO)
                .collect(Collectors.toList());
    }

    // 🔹 3. Estadísticas del alumno (promedios para radar chart)
    public RendimientoAlumnoDTO obtenerEstadisticasAlumno(Long idAlumno) {

        List<RendimientoAlumnoDTO> rendimientos =
                rendimientoRepo.findByVuelo_Alumno_Id(idAlumno)
                        .stream()
                        .map(r -> {
                            RendimientoAlumnoDTO dto = new RendimientoAlumnoDTO();
                            dto.setPuntajeGeneral(r.getPuntajeGeneral());
                            dto.setTecnicaAterrizaje(r.getTecnicaAterrizaje());
                            dto.setManiobras(r.getManiobras());
                            dto.setComunicacionRadio(r.getComunicacionRadio());
                            dto.setSeguimientoInstrucciones(r.getSeguimientoInstrucciones());
                            dto.setPuntualidad(r.getPuntualidad());
                            dto.setComportamiento(r.getComportamiento());
                            return dto;
                        })
                        .collect(Collectors.toList());

        if (rendimientos.isEmpty()) return null;

        // ==== CALCULAR PROMEDIOS (double) ====
        double tecnica = prom(rendimientos, RendimientoAlumnoDTO::getTecnicaAterrizaje);
        double maniobras = prom(rendimientos, RendimientoAlumnoDTO::getManiobras);
        double comunicacion = prom(rendimientos, RendimientoAlumnoDTO::getComunicacionRadio);
        double seguimiento = prom(rendimientos, RendimientoAlumnoDTO::getSeguimientoInstrucciones);
        double puntualidad = prom(rendimientos, RendimientoAlumnoDTO::getPuntualidad);
        double comportamiento = prom(rendimientos, RendimientoAlumnoDTO::getComportamiento);

        // ==== CALCULAR PROMEDIO GENERAL ====
        double puntajeGeneral =
                (tecnica + maniobras + comunicacion + seguimiento + puntualidad + comportamiento)
                        / 6.0;

        // ==== ARMAR RESULTADO ====
        RendimientoAlumnoDTO result = new RendimientoAlumnoDTO();
        result.setTecnicaAterrizaje(BigDecimal.valueOf(tecnica));
        result.setManiobras(BigDecimal.valueOf(maniobras));
        result.setComunicacionRadio(BigDecimal.valueOf(comunicacion));
        result.setSeguimientoInstrucciones(BigDecimal.valueOf(seguimiento));
        result.setPuntualidad(BigDecimal.valueOf(puntualidad));
        result.setComportamiento(BigDecimal.valueOf(comportamiento));
        result.setPuntajeGeneral(BigDecimal.valueOf(puntajeGeneral));

        return result;
    }

    // ==== helper para promedio ====
    private double prom(List<RendimientoAlumnoDTO> list,
                        java.util.function.Function<RendimientoAlumnoDTO, BigDecimal> getter) {

        return list.stream()
                .map(dto -> {
                    BigDecimal val = getter.apply(dto);
                    return val != null ? val.doubleValue() : 0.0;
                })
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    // ==== helper para mapear vuelo a DTO (lo reutilizamos) ====
    private VueloDTO mapVueloToDTO(Vuelo v) {
        VueloDTO dto = new VueloDTO();
        dto.setIdVuelo(v.getIdVuelo());
        dto.setFecha(v.getFecha());
        dto.setHoraInicio(v.getHoraInicio());
        dto.setHoraFin(v.getHoraFin());
        dto.setEstado(v.getEstado());
        dto.setObservacion(v.getObservacion());

        if (v.getAvioneta() != null) {
            dto.setCodigoAvioneta(v.getAvioneta().getCodigo());
        }
        if (v.getAlumno() != null) {
            dto.setNombreAlumno(v.getAlumno().getNombre() + " " + v.getAlumno().getApellido());
        }
        if (v.getTutor() != null) {
            dto.setNombreTutor(v.getTutor().getNombre() + " " + v.getTutor().getApellido());
        }

        return dto;
    }
}