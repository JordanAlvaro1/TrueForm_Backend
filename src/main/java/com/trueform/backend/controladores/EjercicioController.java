package com.trueform.backend.controladores;
import com.trueform.backend.dto.EjercicioDTO;
import com.trueform.backend.entidades.Ejercicio;
import com.trueform.backend.servicios.EjercicioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
@RestController @RequestMapping("/api/ejercicios")
public class EjercicioController {
    @Autowired private EjercicioServicio ejercicioServicio;

    @Autowired private ModelMapper modelMapper;

    private boolean datosValidos(EjercicioDTO dto) {
        return dto.getNombreEjercicio() != null && !dto.getNombreEjercicio().isBlank() && dto.getNombreEjercicio().length() <= 150
            && dto.getGrupoMuscular() != null && !dto.getGrupoMuscular().isBlank() && dto.getGrupoMuscular().length() <= 100
            && (dto.getTipo() == null || dto.getTipo().length() <= 50)
            && (dto.getEquipoNecesario() == null || dto.getEquipoNecesario().length() <= 150);
    }
    private List<EjercicioDTO> convertirLista(List<Ejercicio> ejercicios) {
        List<EjercicioDTO> respuesta = new ArrayList<>();
        for (Ejercicio ejercicio : ejercicios) respuesta.add(modelMapper.map(ejercicio, EjercicioDTO.class));
        return respuesta;
    }
    @GetMapping public
    List<EjercicioDTO> listar() { return convertirLista(ejercicioServicio.listar()); }
    @GetMapping("/{id}")
    public ResponseEntity<EjercicioDTO> buscarPorId(@PathVariable Long id) {
        Ejercicio ejercicio = ejercicioServicio.buscarPorId(id);
        if (ejercicio == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(modelMapper.map(ejercicio, EjercicioDTO.class));
    }
    @PostMapping
    public ResponseEntity<EjercicioDTO> registrar(@RequestBody EjercicioDTO dto) {
        if (!datosValidos(dto)) return ResponseEntity.badRequest().build();
        Ejercicio ejercicio = modelMapper.map(dto, Ejercicio.class); ejercicio.setIdEjercicio(null);
        return ResponseEntity.ok(modelMapper.map(ejercicioServicio.registrar(ejercicio), EjercicioDTO.class));
    }
    @PutMapping
    public ResponseEntity<EjercicioDTO> modificar(@RequestBody EjercicioDTO dto) {
        if (dto.getIdEjercicio() == null || !datosValidos(dto)) return ResponseEntity.badRequest().build();
        if (ejercicioServicio.buscarPorId(dto.getIdEjercicio()) == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(modelMapper.map(ejercicioServicio.modificar(modelMapper.map(dto, Ejercicio.class)), EjercicioDTO.class));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (ejercicioServicio.buscarPorId(id) == null) return ResponseEntity.notFound().build();
        try { ejercicioServicio.eliminar(id); }
        catch (org.springframework.dao.DataIntegrityViolationException ex) { return ResponseEntity.status(409).build(); }
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/grupo/{grupoMuscular}")
    public List<EjercicioDTO> buscarPorGrupoMuscular(@PathVariable String grupoMuscular) {
        return convertirLista(ejercicioServicio.buscarPorGrupoMuscular(grupoMuscular));
    }
    @GetMapping("/{id}/alternativas") public ResponseEntity<List<EjercicioDTO>> alternativas(@PathVariable Long id) {
        Ejercicio original = ejercicioServicio.buscarPorId(id);
        if (original == null) return ResponseEntity.notFound().build();
        List<Ejercicio> alternativas = new ArrayList<>();
        for (Ejercicio ejercicio : ejercicioServicio.buscarPorGrupoMuscular(original.getGrupoMuscular())) {
            if (!ejercicio.getIdEjercicio().equals(id)) alternativas.add(ejercicio);
        }
        return ResponseEntity.ok(convertirLista(alternativas));
    }
}
