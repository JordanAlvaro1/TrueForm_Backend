package com.trueform.backend.controladores;

import com.trueform.backend.dto.*;
import com.trueform.backend.entidades.Progreso;
import com.trueform.backend.entidades.Rutina;
import com.trueform.backend.entidades.Usuario;
import com.trueform.backend.servicios.ProgresoServicio;
import com.trueform.backend.servicios.RutinaServicio;
import com.trueform.backend.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/progresos")
public class ProgresoController {
    @Autowired
    private ProgresoServicio progresoServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private RutinaServicio rutinaServicio;

    @GetMapping
    public List<ProgresoDTO> listar() {

        return progresoServicio.listar()
                .stream()
                .map(progreso -> convertirADTO(progreso))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgresoDTO> buscarPorId(
            @PathVariable Long id) {

        Progreso progreso =
                progresoServicio.buscarPorId(id);

        if (progreso == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(convertirADTO(progreso));
    }

    @PostMapping
    public ResponseEntity<ProgresoDTO> registrar(
            @RequestBody ProgresoDTO progresoDTO) {
        if (progresoDTO.getIdUsuario() == null || progresoDTO.getIdRutina() == null) return ResponseEntity.badRequest().build();

        if ((progresoDTO.getPesoActual() != null && (progresoDTO.getPesoActual().signum() <= 0 ||
            progresoDTO.getPesoActual().compareTo(new java.math.BigDecimal("999.99")) > 0)) ||
            (progresoDTO.getPorcentajeCumplimiento() != null && (progresoDTO.getPorcentajeCumplimiento().signum() < 0 ||
            progresoDTO.getPorcentajeCumplimiento().compareTo(new java.math.BigDecimal("100")) > 0))) {
            return ResponseEntity.badRequest().build();
        }

        Usuario usuario =
                usuarioServicio.buscarPorId(
                        progresoDTO.getIdUsuario());

        if (usuario == null) {
            return ResponseEntity.badRequest().build();
        }

        Rutina rutina =
                rutinaServicio.buscarPorId(
                        progresoDTO.getIdRutina());

        if (rutina == null) {
            return ResponseEntity.badRequest().build();
        }

        Progreso progreso = new Progreso();

        if (!rutina.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
            return ResponseEntity.badRequest().build();
        }
        progreso.setUsuario(usuario);
        progreso.setRutina(rutina);
        progreso.setPesoActual(
                progresoDTO.getPesoActual());
        progreso.setPorcentajeCumplimiento(
                progresoDTO.getPorcentajeCumplimiento());
        progreso.setObservaciones(
                progresoDTO.getObservaciones());

        Progreso registrado =
                progresoServicio.registrar(progreso);

        return ResponseEntity.ok(convertirADTO(registrado));
    }

    @PutMapping
    public ResponseEntity<ProgresoDTO> modificar(
            @RequestBody ProgresoDTO progresoDTO) {
        if (progresoDTO.getIdProgreso() == null) return ResponseEntity.badRequest().build();
        if (progresoDTO.getIdUsuario() == null || progresoDTO.getIdRutina() == null) return ResponseEntity.badRequest().build();

        if ((progresoDTO.getPesoActual() != null && (progresoDTO.getPesoActual().signum() <= 0 ||
            progresoDTO.getPesoActual().compareTo(new java.math.BigDecimal("999.99")) > 0)) ||
            (progresoDTO.getPorcentajeCumplimiento() != null && (progresoDTO.getPorcentajeCumplimiento().signum() < 0 ||
            progresoDTO.getPorcentajeCumplimiento().compareTo(new java.math.BigDecimal("100")) > 0))) {
            return ResponseEntity.badRequest().build();
        }

        Progreso progreso =
                progresoServicio.buscarPorId(
                        progresoDTO.getIdProgreso());

        if (progreso == null) {
            return ResponseEntity.notFound().build();
        }

        Usuario usuario =
                usuarioServicio.buscarPorId(
                        progresoDTO.getIdUsuario());

        if (usuario == null) {
            return ResponseEntity.badRequest().build();
        }

        Rutina rutina =
                rutinaServicio.buscarPorId(
                        progresoDTO.getIdRutina());

        if (rutina == null) {
            return ResponseEntity.badRequest().build();
        }

        if (!rutina.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
            return ResponseEntity.badRequest().build();
        }
        progreso.setUsuario(usuario);
        progreso.setRutina(rutina);
        progreso.setPesoActual(
                progresoDTO.getPesoActual());
        progreso.setPorcentajeCumplimiento(
                progresoDTO.getPorcentajeCumplimiento());
        progreso.setObservaciones(
                progresoDTO.getObservaciones());

        Progreso modificado =
                progresoServicio.modificar(progreso);

        return ResponseEntity.ok(convertirADTO(modificado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        Progreso progreso =
                progresoServicio.buscarPorId(id);

        if (progreso == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            progresoServicio.eliminar(id);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            return ResponseEntity.status(409).build();
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<ProgresoDTO> buscarPorUsuario(
            @PathVariable Long idUsuario) {

        return progresoServicio.buscarPorUsuario(idUsuario)
                .stream()
                .map(progreso -> convertirADTO(progreso))
                .toList();
    }

    // HU9 - Finalizar rutina y registrar progreso
    @PostMapping("/finalizar")
    public ResponseEntity<FinalizarRutinaDTO> finalizarRutina(
            @RequestParam Long idUsuario,
            @RequestParam Long idRutina,
            @RequestParam java.math.BigDecimal porcentajeCumplimiento) {

        // Validar que la rutina pertenezca al usuario
        Rutina rutina = rutinaServicio.buscarPorId(idRutina);

        if (rutina == null) {
            return ResponseEntity.badRequest().build();
        }

        if (!rutina.getUsuario().getIdUsuario().equals(idUsuario)) {
            return ResponseEntity.badRequest().build();
        }

        FinalizarRutinaDTO resultado =
                progresoServicio.finalizarRutina(
                        idUsuario,
                        idRutina,
                        porcentajeCumplimiento
                );

        if (resultado == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(resultado);
    }

    // HU10 - Visualizar progreso semanal
    @GetMapping("/usuario/{idUsuario}/semanal")
    public ResponseEntity<ProgresoSemanalDTO> obtenerProgresoSemanal(
            @PathVariable Long idUsuario) {

        Usuario usuario = usuarioServicio.buscarPorId(idUsuario);

        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        ProgresoSemanalDTO progresoSemanal =
                progresoServicio.obtenerProgresoSemanal(idUsuario);

        return ResponseEntity.ok(progresoSemanal);
    }

    // HU12 - Recomendaciones automáticas basadas en el progreso
    @GetMapping("/usuario/{idUsuario}/recomendacion")
    public ResponseEntity<RecomendacionProgresoDTO> obtenerRecomendacionProgreso(
            @PathVariable Long idUsuario) {

        RecomendacionProgresoDTO resultado =
                progresoServicio.obtenerRecomendacionProgreso(idUsuario);

        if (resultado == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(resultado);
    }

    // HU14 - Obtener recomendaciones avanzadas de optimización
    @GetMapping("/usuario/{idUsuario}/recomendacion-avanzada")
    public ResponseEntity<RecomendacionAvanzadaDTO> obtenerRecomendacionAvanzada(
            @PathVariable Long idUsuario) {

        RecomendacionAvanzadaDTO resultado =
                progresoServicio.obtenerRecomendacionAvanzada(idUsuario);

        if (resultado == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(resultado);
    }


    // HU16 - Obtener historial detallado de progreso físico
    @GetMapping("/usuario/{idUsuario}/historial")
    public ResponseEntity<HistorialProgresoDTO> obtenerHistorialProgreso(
            @PathVariable Long idUsuario) {

        HistorialProgresoDTO resultado =
                progresoServicio.obtenerHistorialProgreso(idUsuario);

        if (resultado == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(resultado);
    }

    private ProgresoDTO convertirADTO(Progreso progreso) {

        ProgresoDTO dto = new ProgresoDTO();

        dto.setIdProgreso(
                progreso.getIdProgreso());

        dto.setIdUsuario(
                progreso.getUsuario().getIdUsuario());

        dto.setIdRutina(
                progreso.getRutina().getIdRutina());

        dto.setFechaRegistro(
                progreso.getFechaRegistro());

        dto.setPesoActual(
                progreso.getPesoActual());

        dto.setPorcentajeCumplimiento(
                progreso.getPorcentajeCumplimiento());

        dto.setObservaciones(
                progreso.getObservaciones());

        return dto;
    }

}
