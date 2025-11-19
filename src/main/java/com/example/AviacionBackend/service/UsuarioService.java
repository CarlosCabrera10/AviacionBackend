package com.example.AviacionBackend.service;


import com.example.AviacionBackend.model.Usuario;
import com.example.AviacionBackend.model.dto.PerfilUpdateDTO;
import com.example.AviacionBackend.model.dto.UsuarioDTO;
import com.example.AviacionBackend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {


    private final EmailService emailService;
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public Usuario guardar(Usuario usuario) {
// Guardamos la contraseña original ANTES de encriptarla
        String contraseñaOriginal = usuario.getContrasena();

        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new RuntimeException("El correo ingresado ya está en uso.");
        }



        // Encriptar
        if (usuario.getContrasena() != null && !usuario.getContrasena().startsWith("$2a$")) {
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        }

        Usuario guardado = usuarioRepository.save(usuario);

        // ⭐ Enviar correo de bienvenida
        try {
            enviarCorreoBienvenida(guardado, contraseñaOriginal);
        } catch (Exception e) {
            System.out.println("❌ Error enviando correo de bienvenida: " + e.getMessage());
        }

        return guardado;
    }

    // ============================================================
    //        ⭐ NUEVO: Correo HTML elegante de bienvenida
    // ============================================================
    private void enviarCorreoBienvenida(Usuario u, String passwordPlano) {

        if (u.getCorreo() == null || passwordPlano == null) return;

        String html = """
                <div style="font-family: 'Segoe UI', Arial, sans-serif; background: #f5f7fa; padding: 30px;">
                
                  <div style="max-width: 600px; margin: auto; background: #ffffff; border-radius: 12px;
                              box-shadow: 0 5px 20px rgba(0,0,0,0.12); overflow: hidden;">
                
                    <div style="background: linear-gradient(135deg, #003060, #001F3F);
                                padding: 25px; text-align: center; color: white;">
                      <h1 style="margin: 0; font-size: 26px;">Bienvenido a la Escuela de Aviación</h1>
                      <p style="margin: 5px 0 0; font-size: 14px; color: #C9A227;">
                        Credenciales de acceso
                      </p>
                    </div>
                
                    <div style="padding: 25px; color: #003060;">
                
                      <p style="font-size: 18px; font-weight: 600;">
                        Hola <span style="color:#001F3F;">%s %s</span>,
                      </p>
                
                      <p style="font-size: 15px; line-height: 1.6;">
                        Tu usuario ha sido creado exitosamente en nuestro sistema.
                        Estas son tus credenciales de acceso:
                      </p>
                
                      <div style="background: #f0f4fa; padding: 15px 20px; border-left: 4px solid #C9A227;
                                  margin: 20px 0; border-radius: 6px;">
                        <p style="margin: 0; font-size: 16px; color: #001F3F;">
                          <strong>Correo:</strong> %s<br>
                          <strong>Contraseña temporal:</strong> %s
                        </p>
                      </div>
                
                      <p style="font-size: 14px; line-height: 1.6;">
                        Te recomendamos iniciar sesión y cambiar tu contraseña desde tu perfil lo antes posible.
                      </p>
                
                      <a href="http://localhost:4200/login"
                         style="display: inline-block; margin-top: 10px;
                                background: #C9A227; padding: 12px 25px; color: white;
                                font-size: 15px; text-decoration: none; font-weight: bold;
                                border-radius: 8px; letter-spacing: 0.5px;">
                        Iniciar sesión
                      </a>
                
                      <br><br>
                      <p style="font-size: 12px; color: #7a7a7a;">
                        Este mensaje fue generado automáticamente.
                      </p>
                    </div>
                
                    <div style="background: #001F3F; padding: 15px; text-align: center; color: #ffffff;
                                font-size: 12px;">
                      © 2025 Escuela de Aviación • Sistema de Gestión de Vuelos
                    </div>
                
                  </div>
                </div>
                """.formatted(
                u.getNombre(),
                u.getApellido(),
                u.getCorreo(),
                passwordPlano
        );

        emailService.enviarCorreo(
                u.getCorreo(),
                "Bienvenido - Credenciales de acceso",
                html
        );
    }


    public Usuario actualizar(Long id, Usuario datos) {

        // Supongamos que enviamos el id del usuario autenticado en datos.id (Temporal)
        if (Boolean.FALSE.equals(datos.getActivo()) && datos.getId().equals(id)) {
            throw new RuntimeException("No puedes desactivar tu propio usuario");
        }

        return usuarioRepository.findById(id)
                .map(u -> {

                    boolean correoEnUso = usuarioRepository.existsByCorreoAndIdNot(
                            datos.getCorreo(),
                            u.getId()
                    );

                    if (correoEnUso) {
                        throw new RuntimeException("El correo ya está en uso.");
                    }


                    u.setNombre(datos.getNombre());
                    u.setApellido(datos.getApellido());
                    u.setCorreo(datos.getCorreo());
                    u.setTelefono(datos.getTelefono());
                    u.setRol(datos.getRol());
                    u.setActivo(datos.getActivo());

                    // Solo actualizar contraseña si se envía y no está vacía
                    if (datos.getContrasena() != null && !datos.getContrasena().isBlank()) {

                        // Encriptar si no está encriptada
                        if (!datos.getContrasena().startsWith("$2a$")) {
                            u.setContrasena(passwordEncoder.encode(datos.getContrasena()));
                        } else {
                            u.setContrasena(datos.getContrasena());
                        }
                    }

                    return usuarioRepository.save(u);
                })
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }


    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }


    public UsuarioDTO obtenerPerfil(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setCorreo(usuario.getCorreo());
        dto.setTelefono(usuario.getTelefono());
        dto.setRol(String.valueOf(usuario.getRol()));
        dto.setActivo(usuario.getActivo());

        return dto;
    }

    public UsuarioDTO actualizarPerfil(Long id, PerfilUpdateDTO data) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // ==============================
        // VALIDAR CORREO DUPLICADO
        // ==============================
        if (data.getCorreo() == null || data.getCorreo().isBlank()) {
            throw new RuntimeException("El correo no puede estar vacío");
        }

        boolean correoEnUso = usuarioRepository.existsByCorreoAndIdNot(
                data.getCorreo(),
                usuario.getId()
        );


        if (correoEnUso) {
            throw new RuntimeException("El correo ingresado ya está registrado por otro usuario");
        }

        // ==============================
        // ACTUALIZAR CAMPOS
        // ==============================
        if (!usuario.getRol().equals("Alumno")) {
            usuario.setNombre(data.getNombre());
            usuario.setApellido(data.getApellido());
        }

        usuario.setCorreo(data.getCorreo());
        usuario.setTelefono(data.getTelefono());

        // ==============================
        // CAMBIAR CONTRASEÑA SI HAY INPUT
        // ==============================
        if (data.getNuevaContrasena() != null && !data.getNuevaContrasena().isBlank()) {
            usuario.setContrasena(passwordEncoder.encode(data.getNuevaContrasena()));
        }

        usuarioRepository.save(usuario);

        return obtenerPerfil(id);
    }


}
