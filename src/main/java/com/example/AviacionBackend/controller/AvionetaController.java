package com.example.AviacionBackend.controller;

import com.example.AviacionBackend.model.Avioneta;
import com.example.AviacionBackend.service.AvionetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avionetas")
@CrossOrigin(origins = "http://localhost:4200") // Permitir Angular local
@RequiredArgsConstructor
public class AvionetaController {

    private final AvionetaService avionetaService;

    @GetMapping
    public List<Avioneta> listar() {
        return avionetaService.listar();
    }

    @GetMapping("/{id}")
    public Avioneta obtenerPorId(@PathVariable Long id) {
        return avionetaService.obtenerPorId(id);
    }

    @PostMapping
    public Avioneta crear(@RequestBody Avioneta avioneta) {
        return avionetaService.guardar(avioneta);
    }

    @PutMapping("/{id}")
    public Avioneta actualizar(@PathVariable Long id, @RequestBody Avioneta avioneta) {
        return avionetaService.actualizar(id, avioneta);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        avionetaService.eliminar(id);
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Avioneta> desactivar(@PathVariable Long id) {
        Avioneta av = avionetaService.desactivar(id);
        return ResponseEntity.ok(av);  // devuelve JSON del objeto
    }


}
