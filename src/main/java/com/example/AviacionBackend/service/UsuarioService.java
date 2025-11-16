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
        // ✅ Encriptar la contraseña solo si no está vacía ni ya encriptada
        if (usuario.getContrasena() != null && !usuario.getContrasena().startsWith("$2a$")) {
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        }

        return usuarioRepository.save(usuario);
    }
    public Usuario actualizar(Long id, Usuario datos) {

        // Supongamos que enviamos el id del usuario autenticado en datos.id (Temporal)
        if (Boolean.FALSE.equals(datos.getActivo()) && datos.getId().equals(id)) {
            throw new RuntimeException("No puedes desactivar tu propio usuario");
        }

        return usuarioRepository.findById(id)
                .map(u -> {
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
        dto.setIdUsuario(usuario.getId());
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
