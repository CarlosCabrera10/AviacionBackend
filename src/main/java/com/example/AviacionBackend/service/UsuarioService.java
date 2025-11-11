package com.example.AviacionBackend.service;


import com.example.AviacionBackend.model.Usuario;
import com.example.AviacionBackend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizar(Long id, Usuario datos) {
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
                        u.setContrasena(datos.getContrasena());
                    }

                    return usuarioRepository.save(u);
                })
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }


    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }
}
