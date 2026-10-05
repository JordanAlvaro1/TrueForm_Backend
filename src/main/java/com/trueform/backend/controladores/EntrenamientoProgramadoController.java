package com.trueform.backend.controladores;

import com.trueform.backend.dto.*;
import com.trueform.backend.entidades.*;
import com.trueform.backend.servicios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/calendario")
public class EntrenamientoProgramadoController {
    @Autowired
    private EntrenamientoProgramadoServicio programadoServicio;
    @Autowired
    private RutinaServicio rutinaServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;

    private boolean datosValidos(EntrenamientoProgramadoDTO dto) {
        return dto.getIdRutina() != null && dto.getFecha() != null && dto.getEstado() != null
            && (dto.getEstado().equals("pendiente") || dto.getEstado().equals("completado") || dto.getEstado().equals("cancelado"));
    }
    @PostMapping
    public ResponseEntity<EntrenamientoProgramadoDTO> registrar(@RequestBody EntrenamientoProgramadoDTO dto) {
        if (!datosValidos(dto)) return ResponseEntity.badRequest().build();
        if (dto.getEstado().equals("completado") && dto.getFecha().isAfter(LocalDate.now())) return ResponseEntity.badRequest().build();
        Rutina rutina = rutinaServicio.buscarPorId(dto.getIdRutina());
        if (rutina == null) return ResponseEntity.badRequest().build();
        EntrenamientoProgramado entrenamiento = new EntrenamientoProgramado();
        entrenamiento.setRutina(rutina);
        entrenamiento.setFecha(dto.getFecha());
        entrenamiento.setEstado(dto.getEstado());
        return ResponseEntity.ok(convertirADTO(programadoServicio.registrar(entrenamiento)));
    }
    @PutMapping
    public ResponseEntity<EntrenamientoProgramadoDTO> modificar(@RequestBody EntrenamientoProgramadoDTO dto) {
        if (dto.getIdProgramacion() == null || !datosValidos(dto)) return ResponseEntity.badRequest().build();
        if (dto.getEstado().equals("completado") && dto.getFecha().isAfter(LocalDate.now())) return ResponseEntity.badRequest().build();
        EntrenamientoProgramado entrenamiento = programadoServicio.buscarPorId(dto.getIdProgramacion());
        if (entrenamiento == null) return ResponseEntity.notFound().build();
        Rutina rutina = rutinaServicio.buscarPorId(dto.getIdRutina());
        if (rutina == null) return ResponseEntity.badRequest().build();
        if (!entrenamiento.getRutina().getUsuario().getIdUsuario().equals(rutina.getUsuario().getIdUsuario())) {
            return ResponseEntity.badRequest().build();
        }
        entrenamiento.setRutina(rutina);
        entrenamiento.setFecha(dto.getFecha());
        entrenamiento.setEstado(dto.getEstado());
        return ResponseEntity.ok(convertirADTO(programadoServicio.modificar(entrenamiento)));
    }
    @GetMapping("/{id}")
    public ResponseEntity<EntrenamientoProgramadoDTO> buscarPorId(@PathVariable Long id) {
        EntrenamientoProgramado entrenamiento = programadoServicio.buscarPorId(id);
        if (entrenamiento == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(convertirADTO(entrenamiento));
    }
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<EntrenamientoProgramadoDTO>> buscarPorUsuario(@PathVariable Long idUsuario) {
        if (usuarioServicio.buscarPorId(idUsuario) == null) return ResponseEntity.notFound().build();
        List<EntrenamientoProgramadoDTO> respuesta = new ArrayList<>();
        for (EntrenamientoProgramado entrenamiento : programadoServicio.buscarPorUsuario(idUsuario)) respuesta.add(convertirADTO(entrenamiento));
        return ResponseEntity.ok(respuesta);
    }
    @GetMapping("/usuario/{idUsuario}/resumen")
    public ResponseEntity<ResumenSemanalDTO> resumen(@PathVariable Long idUsuario, @RequestParam String fecha) {
        if (usuarioServicio.buscarPorId(idUsuario) == null) return ResponseEntity.notFound().build();
        try {
            LocalDate dia = LocalDate.parse(fecha);
            return ResponseEntity.ok(programadoServicio.resumenSemanal(idUsuario, dia));
        } catch (DateTimeParseException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (programadoServicio.buscarPorId(id) == null) return ResponseEntity.notFound().build();
        programadoServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    private EntrenamientoProgramadoDTO convertirADTO(EntrenamientoProgramado entrenamiento) {
        EntrenamientoProgramadoDTO dto = new EntrenamientoProgramadoDTO();
        dto.setIdProgramacion(entrenamiento.getIdProgramacion());
        dto.setIdRutina(entrenamiento.getRutina().getIdRutina());
        dto.setIdUsuario(entrenamiento.getRutina().getUsuario().getIdUsuario());
        dto.setNombreRutina(entrenamiento.getRutina().getNombreRutina());
        dto.setFecha(entrenamiento.getFecha());
        dto.setEstado(entrenamiento.getEstado());
        return dto;
    }
}
