package com.trueform.backend.controladores;

import com.trueform.backend.dto.AjusteRutinaDTO;
import com.trueform.backend.dto.RutinaDTO;
import com.trueform.backend.entidades.Rutina;
import com.trueform.backend.entidades.Usuario;
import com.trueform.backend.servicios.RutinaServicio;
import com.trueform.backend.servicios.UsuarioServicio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import com.trueform.backend.dto.RutinaSemanalDTO;

import org.springframework.security.core.parameters.P;

import java.util.List;


@RestController
@RequestMapping("/api/rutinas")
public class RutinaController {
    @Autowired
    private RutinaServicio rutinaServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<RutinaDTO> listar() {

        return rutinaServicio.listar()
                .stream()
                .map(rutina -> convertirADTO(rutina))
                .toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("@propiedadSeguridad.esRutinaActual(#p0, authentication)")
    public ResponseEntity<RutinaDTO> buscarPorId(@PathVariable Long id) {

        Rutina rutina = rutinaServicio.buscarPorId(id);

        if (rutina == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(convertirADTO(rutina));
    }

    @PostMapping
    @PreAuthorize("@propiedadSeguridad.puedeCrearRutina(#p0, authentication)")
    public ResponseEntity<RutinaDTO> registrar(
            @RequestBody RutinaDTO rutinaDTO) {
        if (rutinaDTO.getIdUsuario() == null ||
            rutinaDTO.getNombreRutina() == null ||
            rutinaDTO.getFechaInicio() == null ||
            rutinaDTO.getEstado() == null) return ResponseEntity.badRequest().build();

        if (rutinaDTO.getNombreRutina().isBlank() || rutinaDTO.getNombreRutina().length() > 150 ||
            rutinaDTO.getEstado().isBlank() || rutinaDTO.getEstado().length() > 20 ||
            (rutinaDTO.getFechaFin() != null && rutinaDTO.getFechaFin().isBefore(rutinaDTO.getFechaInicio()))) {
            return ResponseEntity.badRequest().build();
        }

        Usuario usuario = usuarioServicio.buscarPorId(rutinaDTO.getIdUsuario());

        if (usuario == null) {
            return ResponseEntity.badRequest().build();
        }

        Rutina rutina = new Rutina();

        rutina.setUsuario(usuario);
        rutina.setNombreRutina(rutinaDTO.getNombreRutina());
        rutina.setDescripcion(rutinaDTO.getDescripcion());
        rutina.setFechaInicio(rutinaDTO.getFechaInicio());
        rutina.setFechaFin(rutinaDTO.getFechaFin());
        rutina.setEstado(rutinaDTO.getEstado());

        Rutina rutinaRegistrada =
                rutinaServicio.registrar(rutina);

        return ResponseEntity.ok(convertirADTO(rutinaRegistrada));
    }

    @PutMapping
    @PreAuthorize("@propiedadSeguridad.puedeModificarRutina(#p0, authentication)")
    public ResponseEntity<RutinaDTO> modificar(
            @RequestBody RutinaDTO rutinaDTO) {
        if (rutinaDTO.getIdRutina() == null) return ResponseEntity.badRequest().build();
        if (rutinaDTO.getIdUsuario() == null ||
            rutinaDTO.getNombreRutina() == null ||
            rutinaDTO.getFechaInicio() == null ||
            rutinaDTO.getEstado() == null) return ResponseEntity.badRequest().build();

        if (rutinaDTO.getNombreRutina().isBlank() || rutinaDTO.getNombreRutina().length() > 150 ||
            rutinaDTO.getEstado().isBlank() || rutinaDTO.getEstado().length() > 20 ||
            (rutinaDTO.getFechaFin() != null && rutinaDTO.getFechaFin().isBefore(rutinaDTO.getFechaInicio()))) {
            return ResponseEntity.badRequest().build();
        }

        Rutina rutina =
                rutinaServicio.buscarPorId(rutinaDTO.getIdRutina());

        if (rutina == null) {
            return ResponseEntity.notFound().build();
        }

        Usuario usuario =
                usuarioServicio.buscarPorId(rutinaDTO.getIdUsuario());

        if (usuario == null) {
            return ResponseEntity.badRequest().build();
        }

        if (!rutina.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
            return ResponseEntity.badRequest().build();
        }
        rutina.setUsuario(usuario);
        rutina.setNombreRutina(rutinaDTO.getNombreRutina());
        rutina.setDescripcion(rutinaDTO.getDescripcion());
        rutina.setFechaInicio(rutinaDTO.getFechaInicio());
        rutina.setFechaFin(rutinaDTO.getFechaFin());
        rutina.setEstado(rutinaDTO.getEstado());

        Rutina rutinaModificada =
                rutinaServicio.modificar(rutina);

        return ResponseEntity.ok(convertirADTO(rutinaModificada));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@propiedadSeguridad.esRutinaActual(#p0, authentication)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        Rutina rutina = rutinaServicio.buscarPorId(id);

        if (rutina == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            rutinaServicio.eliminar(id);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            return ResponseEntity.status(409).build();
        }

        return ResponseEntity.noContent().build();
    }

    // HU3 - Consultar rutina semanal del usuario
    @GetMapping("/usuario/{idUsuario}/semanal")
    @PreAuthorize(
            "@propiedadSeguridad.esUsuarioActual(#idUsuario, authentication)"
    )
    public ResponseEntity<RutinaSemanalDTO> obtenerRutinaSemanal(
            @PathVariable("idUsuario")
            @P("idUsuario") Long idUsuario) {

        RutinaSemanalDTO rutinaSemanal =
                rutinaServicio.obtenerRutinaSemanal(idUsuario);

        if (rutinaSemanal == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(rutinaSemanal);
    }


    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize(
            "@propiedadSeguridad.esUsuarioActual(#idUsuario, authentication)"
    )
    public List<RutinaDTO> buscarPorUsuario(
            @PathVariable("idUsuario")
            @P("idUsuario") Long idUsuario) {


        return rutinaServicio.buscarPorUsuario(idUsuario)
                .stream()
                .map(rutina -> convertirADTO(rutina))
                .toList();
    }

    // HU11 - Evaluar estancamiento y recomendar ajuste
    @GetMapping("/usuario/{idUsuario}/ajuste")
    @PreAuthorize(
            "@propiedadSeguridad.esUsuarioActual(#idUsuario, authentication)"
    )
    public ResponseEntity<AjusteRutinaDTO> evaluarAjusteRutina(
            @PathVariable("idUsuario")
            @P("idUsuario") Long idUsuario) {

        AjusteRutinaDTO resultado =
                rutinaServicio.evaluarAjusteRutina(idUsuario);

        if (resultado == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(resultado);
    }

    private RutinaDTO convertirADTO(Rutina rutina) {

        RutinaDTO rutinaDTO =
                modelMapper.map(rutina, RutinaDTO.class);

        rutinaDTO.setIdUsuario(
                rutina.getUsuario().getIdUsuario());

        return rutinaDTO;
    }
}
