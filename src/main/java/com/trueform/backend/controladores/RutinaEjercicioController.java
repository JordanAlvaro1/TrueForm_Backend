package com.trueform.backend.controladores;

import com.trueform.backend.dto.RutinaEjercicioDTO;
import com.trueform.backend.dto.DescansoDTO;
import com.trueform.backend.entidades.Ejercicio;
import com.trueform.backend.entidades.Rutina;
import com.trueform.backend.entidades.RutinaEjercicio;
import com.trueform.backend.servicios.EjercicioServicio;
import com.trueform.backend.servicios.RutinaEjercicioServicio;
import com.trueform.backend.servicios.RutinaServicio;
import com.trueform.backend.dto.EjercicioGuiadoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/rutina-ejercicios")
public class RutinaEjercicioController {
    @Autowired
    private RutinaEjercicioServicio rutinaEjercicioServicio;

    @Autowired
    private RutinaServicio rutinaServicio;

    @Autowired
    private EjercicioServicio ejercicioServicio;


    @GetMapping
    public List<RutinaEjercicioDTO> listar() {

        return rutinaEjercicioServicio.listar()
                .stream()
                .map(rutinaEjercicio -> convertirADTO(rutinaEjercicio))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RutinaEjercicioDTO> buscarPorId(
            @PathVariable Long id) {

        RutinaEjercicio rutinaEjercicio =
                rutinaEjercicioServicio.buscarPorId(id);

        if (rutinaEjercicio == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(convertirADTO(rutinaEjercicio));
    }

    @PostMapping
    public ResponseEntity<RutinaEjercicioDTO> registrar(
            @RequestBody RutinaEjercicioDTO rutinaEjercicioDTO) {
        if (rutinaEjercicioDTO.getIdRutina() == null ||
            rutinaEjercicioDTO.getIdEjercicio() == null ||
            rutinaEjercicioDTO.getSeries() == null ||
            rutinaEjercicioDTO.getRepeticiones() == null) return ResponseEntity.badRequest().build();

        if (rutinaEjercicioDTO.getSeries() <= 0 || rutinaEjercicioDTO.getRepeticiones() <= 0 ||
            (rutinaEjercicioDTO.getDescansoSegundos() != null && rutinaEjercicioDTO.getDescansoSegundos() < 0) ||
            (rutinaEjercicioDTO.getOrdenEjercicio() != null && rutinaEjercicioDTO.getOrdenEjercicio() < 0) ||
            (rutinaEjercicioDTO.getDiaSemana() != null && rutinaEjercicioDTO.getDiaSemana().length() > 20)) {
            return ResponseEntity.badRequest().build();
        }

        Rutina rutina =
                rutinaServicio.buscarPorId(rutinaEjercicioDTO.getIdRutina());

        if (rutina == null) {
            return ResponseEntity.badRequest().build();
        }

        Ejercicio ejercicio =
                ejercicioServicio.buscarPorId(rutinaEjercicioDTO.getIdEjercicio());

        if (ejercicio == null) {
            return ResponseEntity.badRequest().build();
        }

        RutinaEjercicio rutinaEjercicio = new RutinaEjercicio();

        rutinaEjercicio.setRutina(rutina);
        rutinaEjercicio.setEjercicio(ejercicio);
        rutinaEjercicio.setSeries(rutinaEjercicioDTO.getSeries());
        rutinaEjercicio.setRepeticiones(rutinaEjercicioDTO.getRepeticiones());
        rutinaEjercicio.setDescansoSegundos(
                rutinaEjercicioDTO.getDescansoSegundos());
        rutinaEjercicio.setDiaSemana(rutinaEjercicioDTO.getDiaSemana());
        rutinaEjercicio.setOrdenEjercicio(
                rutinaEjercicioDTO.getOrdenEjercicio());

        RutinaEjercicio registrado =
                rutinaEjercicioServicio.registrar(rutinaEjercicio);

        return ResponseEntity.ok(convertirADTO(registrado));
    }

    @PutMapping
    public ResponseEntity<RutinaEjercicioDTO> modificar(
            @RequestBody RutinaEjercicioDTO rutinaEjercicioDTO) {
        if (rutinaEjercicioDTO.getIdRutinaEjercicio() == null) return ResponseEntity.badRequest().build();
        if (rutinaEjercicioDTO.getIdRutina() == null ||
            rutinaEjercicioDTO.getIdEjercicio() == null ||
            rutinaEjercicioDTO.getSeries() == null ||
            rutinaEjercicioDTO.getRepeticiones() == null) return ResponseEntity.badRequest().build();

        if (rutinaEjercicioDTO.getSeries() <= 0 || rutinaEjercicioDTO.getRepeticiones() <= 0 ||
            (rutinaEjercicioDTO.getDescansoSegundos() != null && rutinaEjercicioDTO.getDescansoSegundos() < 0) ||
            (rutinaEjercicioDTO.getOrdenEjercicio() != null && rutinaEjercicioDTO.getOrdenEjercicio() < 0) ||
            (rutinaEjercicioDTO.getDiaSemana() != null && rutinaEjercicioDTO.getDiaSemana().length() > 20)) {
            return ResponseEntity.badRequest().build();
        }

        RutinaEjercicio rutinaEjercicio =
                rutinaEjercicioServicio.buscarPorId(
                        rutinaEjercicioDTO.getIdRutinaEjercicio());

        if (rutinaEjercicio == null) {
            return ResponseEntity.notFound().build();
        }

        Rutina rutina =
                rutinaServicio.buscarPorId(rutinaEjercicioDTO.getIdRutina());

        if (rutina == null) {
            return ResponseEntity.badRequest().build();
        }

        Ejercicio ejercicio =
                ejercicioServicio.buscarPorId(
                        rutinaEjercicioDTO.getIdEjercicio());

        if (ejercicio == null) {
            return ResponseEntity.badRequest().build();
        }

        rutinaEjercicio.setRutina(rutina);
        rutinaEjercicio.setEjercicio(ejercicio);
        rutinaEjercicio.setSeries(rutinaEjercicioDTO.getSeries());
        rutinaEjercicio.setRepeticiones(
                rutinaEjercicioDTO.getRepeticiones());
        rutinaEjercicio.setDescansoSegundos(
                rutinaEjercicioDTO.getDescansoSegundos());
        rutinaEjercicio.setDiaSemana(
                rutinaEjercicioDTO.getDiaSemana());
        rutinaEjercicio.setOrdenEjercicio(
                rutinaEjercicioDTO.getOrdenEjercicio());

        RutinaEjercicio modificado =
                rutinaEjercicioServicio.modificar(rutinaEjercicio);

        return ResponseEntity.ok(convertirADTO(modificado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        RutinaEjercicio rutinaEjercicio =
                rutinaEjercicioServicio.buscarPorId(id);

        if (rutinaEjercicio == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            rutinaEjercicioServicio.eliminar(id);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            return ResponseEntity.status(409).build();
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/rutina/{idRutina}")
    public List<RutinaEjercicioDTO> buscarPorRutina(
            @PathVariable Long idRutina) {

        return rutinaEjercicioServicio.buscarPorRutina(idRutina)
                .stream()
                .map(rutinaEjercicio -> convertirADTO(rutinaEjercicio))
                .toList();
    }

    @GetMapping("/{id}/descanso")
    public ResponseEntity<DescansoDTO> descanso(@PathVariable Long id) {
        RutinaEjercicio ejercicio = rutinaEjercicioServicio.buscarPorId(id);
        if (ejercicio == null) return ResponseEntity.notFound().build();
        DescansoDTO dto = new DescansoDTO();
        dto.setIdRutinaEjercicio(id);
        dto.setDescansoSegundos(ejercicio.getDescansoSegundos());
        return ResponseEntity.ok(dto);
    }

    // HU8 - Iniciar rutina guiada paso a paso
    @GetMapping("/rutina/{idRutina}/dia/{diaSemana}/guiada")
    public ResponseEntity<EjercicioGuiadoDTO> obtenerEjercicioGuiado(
            @PathVariable Long idRutina,
            @PathVariable String diaSemana,
            @RequestParam(defaultValue = "0") Integer posicion) {

        EjercicioGuiadoDTO ejercicio =
                rutinaEjercicioServicio.obtenerEjercicioGuiado(
                        idRutina,
                        diaSemana,
                        posicion
                );

        if (ejercicio == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(ejercicio);
    }

    private RutinaEjercicioDTO convertirADTO(
            RutinaEjercicio rutinaEjercicio) {

        RutinaEjercicioDTO dto = new RutinaEjercicioDTO();

        dto.setIdRutinaEjercicio(
                rutinaEjercicio.getIdRutinaEjercicio());

        dto.setIdRutina(
                rutinaEjercicio.getRutina().getIdRutina());

        dto.setIdEjercicio(
                rutinaEjercicio.getEjercicio().getIdEjercicio());

        dto.setSeries(
                rutinaEjercicio.getSeries());

        dto.setRepeticiones(
                rutinaEjercicio.getRepeticiones());

        dto.setDescansoSegundos(
                rutinaEjercicio.getDescansoSegundos());

        dto.setDiaSemana(
                rutinaEjercicio.getDiaSemana());

        dto.setOrdenEjercicio(
                rutinaEjercicio.getOrdenEjercicio());

        return dto;
    }

}
