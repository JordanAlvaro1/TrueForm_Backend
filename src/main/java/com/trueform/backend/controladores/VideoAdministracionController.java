package com.trueform.backend.controladores;

import com.trueform.backend.dto.VideoEducativoRequestDTO;
import com.trueform.backend.entidades.VideoEducativo;
import com.trueform.backend.servicios.VideoEducativoServicio;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/videos")
@PreAuthorize("hasRole('ADMIN')")
public class VideoAdministracionController {

    private final VideoEducativoServicio servicio;

    public VideoAdministracionController(VideoEducativoServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<VideoEducativo> listar() {
        return servicio.listarTodos();
    }

    @PostMapping
    public ResponseEntity<VideoEducativo> crear(
            @Valid @RequestBody VideoEducativoRequestDTO datos) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(servicio.crear(datos));
    }

    @PutMapping("/{id}")
    public VideoEducativo actualizar(
            @PathVariable Long id,
            @Valid @RequestBody VideoEducativoRequestDTO datos) {
        return servicio.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        servicio.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reactivar")
    public VideoEducativo reactivar(@PathVariable Long id) {
        return servicio.reactivar(id);
    }
}
