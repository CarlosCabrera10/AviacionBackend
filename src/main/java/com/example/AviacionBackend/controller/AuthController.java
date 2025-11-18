package com.example.AviacionBackend.controller;

import com.example.AviacionBackend.model.Usuario;
import com.example.AviacionBackend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/hash")
    public Map<String, String> generarHash(@RequestBody Map<String, String> request) {
        String contrasena = request.get("contrasena");
        String hash = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode(contrasena);
        return Map.of("hash", hash);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        try {
            Usuario nuevo = authService.registrar(usuario);
            return ResponseEntity.ok(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        try {
            String correo = loginData.get("correo");
            String contrasena = loginData.get("contrasena");

            // 🔥 VALIDAR SI EL USUARIO ESTÁ INACTIVO (ANTES DE LOGEARLO)
            Usuario usuario = authService.buscarPorCorreo(correo);

            if (usuario == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Usuario o contraseña incorrectos"));
            }

            if (!usuario.getActivo()) {
                return ResponseEntity.status(403).body(Map.of("error", "Tu usuario está inactivo, contacta al administrador"));
            }

            // Si todo ok → login normal
            Map<String, Object> respuesta = authService.loginConUsuario(correo, contrasena);

            return ResponseEntity.ok(respuesta);

        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }
}
