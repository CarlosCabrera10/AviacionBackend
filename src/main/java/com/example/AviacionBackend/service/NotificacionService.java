package com.example.AviacionBackend.service;

import com.example.AviacionBackend.model.Notificacion;
import com.example.AviacionBackend.model.Usuario;
import com.example.AviacionBackend.repository.NotificacionRepository;
import com.example.AviacionBackend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository repo;
    private final UsuarioRepository usuarioRepo;

    public void crearNotificacion(Long idUsuario, String mensaje) {
        Usuario u = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Notificacion n = new Notificacion();
        n.setUsuario(u);
        n.setMensaje(mensaje);

        repo.save(n);
    }

    public List<Notificacion> obtenerPorUsuario(Long idUsuario) {
        return repo.findByUsuario_IdOrderByFechaDesc(idUsuario);
    }

    public void marcarLeida(Long idNotificacion) {
        repo.findById(idNotificacion).ifPresent(n -> {
            n.setLeida(true);
            repo.save(n);
        });
    }
}

