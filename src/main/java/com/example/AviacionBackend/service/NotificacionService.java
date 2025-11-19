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
    private final EmailService emailService;

//    public void crearNotificacion(Long idUsuario, String mensaje) {
//        Usuario u = usuarioRepo.findById(idUsuario)
//                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
//
//        Notificacion n = new Notificacion();
//        n.setUsuario(u);
//        n.setMensaje(mensaje);
//
//        repo.save(n);
//    }

    public void crearNotificacion(Long idUsuario, String mensaje) {

        // Buscar usuario (obligatorio)
        Usuario u = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Guardar en BD
        Notificacion n = new Notificacion();
        n.setUsuario(u);  // ✔ ahora si
        n.setMensaje(mensaje);
        repo.save(n);

        // Si no tiene correo, no intentar enviar
        if (u.getCorreo() == null) return;

        // HTML del correo
        String html = """
    <div style="font-family: 'Segoe UI', Arial, sans-serif; background: #f5f7fa; padding: 30px;">
      <div style="max-width: 600px; margin: auto; background: #ffffff; border-radius: 12px;
                  box-shadow: 0 5px 20px rgba(0,0,0,0.12); overflow: hidden;">

        <div style="background: linear-gradient(135deg, #003060, #001F3F);
                    padding: 25px; text-align: center; color: white;">
          <h1 style="margin: 0; font-size: 26px;">Escuela de Aviación</h1>
          <p style="margin: 5px 0 0; font-size: 14px; color: #C9A227;">
            Notificación de Vuelo
          </p>
        </div>

        <div style="padding: 25px; color: #003060;">
          <p style="font-size: 18px; font-weight: 600;">
            Hola <span style="color:#001F3F;">%s</span>,
          </p>

          <p style="font-size: 15px; line-height: 1.6;">
            Te informamos que tienes una nueva actualización en tu estado de vuelo:
          </p>

          <div style="background: #f0f4fa; padding: 15px 20px; border-left: 4px solid #C9A227;
                      margin: 20px 0; border-radius: 6px;">
            <p style="margin: 0; font-size: 15px; color: #001F3F;">
              %s
            </p>
          </div>

          <p style="font-size: 14px; line-height: 1.6;">
            Para ver más detalles ingresa a tu panel de alumno:
          </p>

          <a href="http://localhost:4200/alumno/notificaciones"
             style="display: inline-block; margin-top: 10px;
                    background: #C9A227; padding: 12px 25px; color: white;
                    font-size: 15px; text-decoration: none; font-weight: bold;
                    border-radius: 8px; letter-spacing: 0.5px;">
            Ver mis notificaciones
          </a>

          <br><br>
          <p style="font-size: 12px; color: #7a7a7a;">
            Este mensaje fue generado automáticamente, por favor no respondas este correo.
          </p>
        </div>

        <div style="background: #001F3F; padding: 15px; text-align: center; color: #ffffff;
                    font-size: 12px;">
          © 2025 Escuela de Aviación • Sistema de Gestión de Vuelos
        </div>

      </div>
    </div>
""".formatted(u.getNombre(), mensaje);

        // Enviar correo real
        emailService.enviarCorreo(
                u.getCorreo(),
                "Nueva notificación de vuelo",
                html
        );
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

