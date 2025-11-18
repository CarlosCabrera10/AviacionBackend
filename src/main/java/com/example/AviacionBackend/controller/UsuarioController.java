package com.example.AviacionBackend.controller;

import com.example.AviacionBackend.model.Usuario;
import com.example.AviacionBackend.model.dto.PerfilUpdateDTO;
import com.example.AviacionBackend.model.dto.UsuarioDTO;
import com.example.AviacionBackend.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200") // Permitir Angular local
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.listar();
    }

    @GetMapping("/{id}")
    public Usuario obtenerPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id);
    }


    @PostMapping
    public Usuario crear(@RequestBody Usuario usuario) {
        return usuarioService.guardar(usuario);
    }

    @PutMapping("/{id}")
    public Usuario actualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        return usuarioService.actualizar(id, usuario);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
    }


    @GetMapping("/perfil/{id}")
    public UsuarioDTO obtenerPerfil(@PathVariable Long id) {
        return usuarioService.obtenerPerfil(id);
    }

    @PutMapping("/perfil/{id}")
    public UsuarioDTO actualizarPerfil(@PathVariable Long id, @RequestBody PerfilUpdateDTO dto) {
        return usuarioService.actualizarPerfil(id, dto);
    }


}
