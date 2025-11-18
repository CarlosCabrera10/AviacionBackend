package com.example.AviacionBackend.repository;

import com.example.AviacionBackend.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    //List<Notificacion> findByUsuarioIdOrderByFechaDesc(Long idUsuario);
    List<Notificacion> findByUsuarioIdAndLeidaFalseOrderByFechaDesc(Long idUsuario);
    List<Notificacion> findByUsuario_IdOrderByFechaDesc(Long idUsuario);
}
