package com.trueform.backend.controladores;

import com.trueform.backend.dto.FavoritoDTO;
import com.trueform.backend.entidades.*;
import com.trueform.backend.servicios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/favoritos")
public class FavoritoController {
    @Autowired
    private FavoritoServicio favoritoServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private EjercicioServicio ejercicioServicio;

    @PostMapping
    public ResponseEntity<FavoritoDTO> registrar(@RequestBody FavoritoDTO dto) {
        if (dto.getIdUsuario() == null || dto.getIdEjercicio() == null) return ResponseEntity.badRequest().build();
        Usuario usuario = usuarioServicio.buscarPorId(dto.getIdUsuario());
        Ejercicio ejercicio = ejercicioServicio.buscarPorId(dto.getIdEjercicio());
        if (usuario == null || ejercicio == null) return ResponseEntity.badRequest().build();
        if (favoritoServicio.buscarRepetido(dto.getIdUsuario(), dto.getIdEjercicio()) != null) {
            return ResponseEntity.status(409).build();
        }
        Favorito favorito = new Favorito();
        favorito.setUsuario(usuario);
        favorito.setEjercicio(ejercicio);
        try {
            return ResponseEntity.ok(convertirADTO(favoritoServicio.registrar(favorito)));
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            return ResponseEntity.status(409).build();
        }
    }
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<FavoritoDTO>> buscarPorUsuario(@PathVariable Long idUsuario) {
        if (usuarioServicio.buscarPorId(idUsuario) == null) return ResponseEntity.notFound().build();
        List<FavoritoDTO> respuesta = new ArrayList<>();
        for (Favorito favorito : favoritoServicio.buscarPorUsuario(idUsuario)) respuesta.add(convertirADTO(favorito));
        return ResponseEntity.ok(respuesta);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (favoritoServicio.buscarPorId(id) == null) return ResponseEntity.notFound().build();
        favoritoServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    private FavoritoDTO convertirADTO(Favorito favorito) {
        FavoritoDTO dto = new FavoritoDTO();
        dto.setIdFavorito(favorito.getIdFavorito());
        dto.setIdUsuario(favorito.getUsuario().getIdUsuario());
        dto.setIdEjercicio(favorito.getEjercicio().getIdEjercicio());
        dto.setNombreEjercicio(favorito.getEjercicio().getNombreEjercicio());
        return dto;
    }
}
