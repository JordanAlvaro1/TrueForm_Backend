package com.trueform.backend.controladores;

import com.trueform.backend.dto.TipContenidoRequestDTO;
import com.trueform.backend.entidades.TipContenido;
import com.trueform.backend.servicios.TipAdministracionServicio;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/tips")
@PreAuthorize("hasRole('ADMIN')")
public class TipAdministracionController {

    private final TipAdministracionServicio servicio;

    public TipAdministracionController(TipAdministracionServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<TipContenido> listar() {
        return servicio.listar();
    }

    @PostMapping
    public ResponseEntity<TipContenido> crear(
            @Valid @RequestBody TipContenidoRequestDTO datos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicio.crear(datos));
    }

    @PutMapping("/{id}")
    public TipContenido actualizar(
            @PathVariable Long id,
            @Valid @RequestBody TipContenidoRequestDTO datos) {
        return servicio.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        servicio.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reactivar")
    public TipContenido reactivar(@PathVariable Long id) {
        return servicio.reactivar(id);
    }
}
