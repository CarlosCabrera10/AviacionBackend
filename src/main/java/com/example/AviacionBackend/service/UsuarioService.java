package com.example.AviacionBackend.service;


import com.example.AviacionBackend.model.Usuario;
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
}
