package com.trueform.backend.controladores;

import com.trueform.backend.dto.ObjetivoDTO;
import com.trueform.backend.entidades.Objetivo;
import com.trueform.backend.servicios.ObjetivoServicio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/objetivos")
public class ObjetivoController {
    @Autowired
    private ObjetivoServicio objetivoServicio;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public List<ObjetivoDTO> listar() {

        return objetivoServicio.listar()
                .stream()
                .map(objetivo ->
                        modelMapper.map(objetivo, ObjetivoDTO.class))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObjetivoDTO> buscarPorId(@PathVariable Long id) {

        Objetivo objetivo = objetivoServicio.buscarPorId(id);

        if (objetivo == null) {
            return ResponseEntity.notFound().build();
        }

        ObjetivoDTO objetivoDTO =
                modelMapper.map(objetivo, ObjetivoDTO.class);

        return ResponseEntity.ok(objetivoDTO);
    }

    @PostMapping
    public ResponseEntity<ObjetivoDTO> registrar(
            @RequestBody ObjetivoDTO objetivoDTO) {
        if (objetivoDTO.getNombreObjetivo() == null) return ResponseEntity.badRequest().build();

        if (objetivoDTO.getNombreObjetivo().isBlank() || objetivoDTO.getNombreObjetivo().length() > 100) return ResponseEntity.badRequest().build();

        Objetivo objetivo =
                modelMapper.map(objetivoDTO, Objetivo.class);

        objetivo.setIdObjetivo(null);
        Objetivo objetivoRegistrado =
                objetivoServicio.registrar(objetivo);

        ObjetivoDTO respuesta =
                modelMapper.map(objetivoRegistrado, ObjetivoDTO.class);

        return ResponseEntity.ok(respuesta);
    }

    @PutMapping
    public ResponseEntity<ObjetivoDTO> modificar(
            @RequestBody ObjetivoDTO objetivoDTO) {
        if (objetivoDTO.getIdObjetivo() == null) return ResponseEntity.badRequest().build();
        if (objetivoDTO.getNombreObjetivo() == null) return ResponseEntity.badRequest().build();

        if (objetivoDTO.getNombreObjetivo().isBlank() || objetivoDTO.getNombreObjetivo().length() > 100) return ResponseEntity.badRequest().build();

        Objetivo objetivoExistente =
                objetivoServicio.buscarPorId(objetivoDTO.getIdObjetivo());

        if (objetivoExistente == null) {
            return ResponseEntity.notFound().build();
        }

        objetivoExistente.setNombreObjetivo(
                objetivoDTO.getNombreObjetivo());

        objetivoExistente.setDescripcion(
                objetivoDTO.getDescripcion());

        Objetivo objetivoModificado =
                objetivoServicio.modificar(objetivoExistente);

        ObjetivoDTO respuesta =
                modelMapper.map(objetivoModificado, ObjetivoDTO.class);

        return ResponseEntity.ok(respuesta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        Objetivo objetivo = objetivoServicio.buscarPorId(id);

        if (objetivo == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            objetivoServicio.eliminar(id);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            return ResponseEntity.status(409).build();
        }

        return ResponseEntity.noContent().build();
    }
}
