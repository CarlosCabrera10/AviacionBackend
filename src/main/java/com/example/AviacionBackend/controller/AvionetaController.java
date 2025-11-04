package com.example.AviacionBackend.controller;

import com.example.AviacionBackend.model.Avioneta;
import com.example.AviacionBackend.service.AvionetaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/avionetas")
@CrossOrigin(origins = "http://localhost:4200")
public class AvionetaController {

    private final AvionetaService avionetaService;

    public AvionetaController(AvionetaService avionetaService) {
        this.avionetaService = avionetaService;
    }

    @GetMapping
    public List<Avioneta> listarAvionetas() {
        return avionetaService.obtenerTodas();
    }

    @GetMapping("/{id}")
    public Optional<Avioneta> obtenerAvioneta(@PathVariable Long id) {
        return avionetaService.obtenerPorId(id);
    }

    @PostMapping
    public Avioneta crearAvioneta(@RequestBody Avioneta avioneta) {
        return avionetaService.guardar(avioneta);
    }

    @PutMapping("/{id}")
    public Avioneta actualizarAvioneta(@PathVariable Long id, @RequestBody Avioneta avioneta) {
        avioneta.setIdAvioneta(id);
        return avionetaService.guardar(avioneta);
    }

    @DeleteMapping("/{id}")
    public void eliminarAvioneta(@PathVariable Long id) {
        avionetaService.eliminar(id);
    }
}
