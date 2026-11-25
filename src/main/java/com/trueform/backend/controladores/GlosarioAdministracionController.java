package com.trueform.backend.controladores;

import com.trueform.backend.dto.GlosarioTerminoRequestDTO;
import com.trueform.backend.entidades.GlosarioTermino;
import com.trueform.backend.servicios.GlosarioAdministracionServicio;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/glosario")
@PreAuthorize("hasRole('ADMIN')")
public class GlosarioAdministracionController {

    private final GlosarioAdministracionServicio servicio;

    public GlosarioAdministracionController(GlosarioAdministracionServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<GlosarioTermino> listar() {
        return servicio.listar();
    }

    @PostMapping
    public ResponseEntity<GlosarioTermino> crear(
            @Valid @RequestBody GlosarioTerminoRequestDTO datos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicio.crear(datos));
    }

    @PutMapping("/{id}")
    public GlosarioTermino actualizar(
            @PathVariable Long id,
            @Valid @RequestBody GlosarioTerminoRequestDTO datos) {
        return servicio.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        servicio.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reactivar")
    public GlosarioTermino reactivar(@PathVariable Long id) {
        return servicio.reactivar(id);
    }
}
