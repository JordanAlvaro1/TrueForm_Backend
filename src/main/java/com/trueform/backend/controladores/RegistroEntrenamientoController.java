package com.trueform.backend.controladores;

import com.trueform.backend.dto.RegistroEntrenamientoDTO;
import com.trueform.backend.entidades.*;
import com.trueform.backend.servicios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/registros-entrenamiento")
public class RegistroEntrenamientoController {
    @Autowired
    private RegistroEntrenamientoServicio registroServicio;
    @Autowired
    private RutinaServicio rutinaServicio;
    @Autowired
    private EjercicioServicio ejercicioServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;

    private boolean datosValidos(RegistroEntrenamientoDTO dto) {
        return dto.getIdRutina() != null && dto.getIdEjercicio() != null && dto.getFecha() != null
            && !dto.getFecha().isAfter(LocalDate.now())
            && dto.getSeries() != null && dto.getSeries() > 0 && dto.getSeries() <= 100
            && dto.getRepeticiones() != null && dto.getRepeticiones() > 0 && dto.getRepeticiones() <= 1000
            && dto.getPesoKg() != null && dto.getPesoKg().signum() >= 0
            && dto.getPesoKg().compareTo(new BigDecimal("999999.99")) <= 0 && dto.getPesoKg().scale() <= 2;
    }
    @PostMapping
    public ResponseEntity<RegistroEntrenamientoDTO> registrar(@RequestBody RegistroEntrenamientoDTO dto) {
        if (!datosValidos(dto)) return ResponseEntity.badRequest().build();
        Rutina rutina = rutinaServicio.buscarPorId(dto.getIdRutina());
        Ejercicio ejercicio = ejercicioServicio.buscarPorId(dto.getIdEjercicio());
        if (rutina == null || ejercicio == null) return ResponseEntity.badRequest().build();
        RegistroEntrenamiento registro = new RegistroEntrenamiento();
        registro.setRutina(rutina);
        registro.setEjercicio(ejercicio);
        registro.setNombreRutina(rutina.getNombreRutina());
        registro.setNombreEjercicio(ejercicio.getNombreEjercicio());
        registro.setFecha(dto.getFecha());
        registro.setSeries(dto.getSeries());
        registro.setRepeticiones(dto.getRepeticiones());
        registro.setPesoKg(dto.getPesoKg());
        return ResponseEntity.ok(convertirADTO(registroServicio.registrar(registro)));
    }
    @PutMapping
    public ResponseEntity<RegistroEntrenamientoDTO> modificar(@RequestBody RegistroEntrenamientoDTO dto) {
        if (dto.getIdRegistro() == null || !datosValidos(dto)) return ResponseEntity.badRequest().build();
        RegistroEntrenamiento registro = registroServicio.buscarPorId(dto.getIdRegistro());
        if (registro == null) return ResponseEntity.notFound().build();
        // El historial permite corregir cantidades y fecha. Sus relaciones permanecen iguales.
        if (!registro.getRutina().getIdRutina().equals(dto.getIdRutina()) || !registro.getEjercicio().getIdEjercicio().equals(dto.getIdEjercicio())) {
            return ResponseEntity.badRequest().build();
        }
        registro.setFecha(dto.getFecha());
        registro.setSeries(dto.getSeries());
        registro.setRepeticiones(dto.getRepeticiones());
        registro.setPesoKg(dto.getPesoKg());
        return ResponseEntity.ok(convertirADTO(registroServicio.modificar(registro)));
    }
    @GetMapping("/{id}")
    public ResponseEntity<RegistroEntrenamientoDTO> buscarPorId(@PathVariable Long id) {
        RegistroEntrenamiento registro = registroServicio.buscarPorId(id);
        if (registro == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(convertirADTO(registro));
    }
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<RegistroEntrenamientoDTO>> buscarPorUsuario(@PathVariable Long idUsuario) {
        if (usuarioServicio.buscarPorId(idUsuario) == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(convertirLista(registroServicio.buscarPorUsuario(idUsuario)));
    }
    @GetMapping("/usuario/{idUsuario}/ejercicio/{idEjercicio}")
    public ResponseEntity<List<RegistroEntrenamientoDTO>> historial(@PathVariable Long idUsuario, @PathVariable Long idEjercicio) {
        if (usuarioServicio.buscarPorId(idUsuario) == null ||
            ejercicioServicio.buscarPorId(idEjercicio) == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(convertirLista(registroServicio.buscarPorUsuarioYEjercicio(idUsuario, idEjercicio)));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (registroServicio.buscarPorId(id) == null) return ResponseEntity.notFound().build();
        registroServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    private List<RegistroEntrenamientoDTO> convertirLista(List<RegistroEntrenamiento> registros) {
        List<RegistroEntrenamientoDTO> respuesta = new ArrayList<>();
        for (RegistroEntrenamiento registro : registros) respuesta.add(convertirADTO(registro));
        return respuesta;
    }
    private RegistroEntrenamientoDTO convertirADTO(RegistroEntrenamiento registro) {
        RegistroEntrenamientoDTO dto = new RegistroEntrenamientoDTO();
        dto.setIdRegistro(registro.getIdRegistro());
        dto.setIdRutina(registro.getRutina().getIdRutina());
        dto.setIdEjercicio(registro.getEjercicio().getIdEjercicio());
        dto.setIdUsuario(registro.getRutina().getUsuario().getIdUsuario());
        dto.setNombreRutina(registro.getNombreRutina());
        dto.setNombreEjercicio(registro.getNombreEjercicio());
        dto.setFecha(registro.getFecha());
        dto.setSeries(registro.getSeries());
        dto.setRepeticiones(registro.getRepeticiones());
        dto.setPesoKg(registro.getPesoKg());
        return dto;
    }
}
