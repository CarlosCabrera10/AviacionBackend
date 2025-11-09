package com.example.AviacionBackend.service;

import com.example.AviacionBackend.model.Notificacion;
import com.example.AviacionBackend.repository.NotificacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificacionService {

    private final NotificacionRepository repository;

    public NotificacionService(NotificacionRepository repository) {
        this.repository = repository;
    }

    public List<Notificacion> listarPorUsuario(Long idUsuario) {
        return repository.findByUsuarioIdOrderByFechaDesc(idUsuario);
    }

    public List<Notificacion> listarNoLeidas(Long idUsuario) {
        return repository.findByUsuarioIdAndLeidaFalseOrderByFechaDesc(idUsuario);
    }

    public Notificacion crear(Notificacion notificacion) {
        return repository.save(notificacion);
    }

    public Optional<Notificacion> marcarComoLeida(Long id) {
        Optional<Notificacion> notificacionOpt = repository.findById(id);
        notificacionOpt.ifPresent(n -> {
            n.setLeida(true);
            repository.save(n);
        });
        return notificacionOpt;
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
