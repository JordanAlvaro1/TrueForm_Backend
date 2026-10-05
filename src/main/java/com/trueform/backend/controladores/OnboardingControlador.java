package com.trueform.backend.controladores;

import com.trueform.backend.dto.OnboardingDTO;
import com.trueform.backend.dto.SeleccionObjetivoDTO;
import com.trueform.backend.entidades.Usuario;
import com.trueform.backend.servicios.UsuarioServicio;
import com.trueform.backend.servicios.OnboardingServicio;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;
import com.trueform.backend.dto.UsuarioDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class OnboardingControlador {
    @Autowired
    private OnboardingServicio onboardingServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/onboarding")
    public ResponseEntity<UsuarioDTO> onboarding(@Valid @RequestBody OnboardingDTO dto) {
        if (!dto.getNivel().equals("principiante") && !dto.getNivel().equals("intermedio") && !dto.getNivel().equals("avanzado")) {
            return ResponseEntity.badRequest().build();
        }
        for (String equipo : dto.getEquipo()) {
            if (equipo == null || equipo.isBlank()) return ResponseEntity.badRequest().build();
        }
        Usuario usuario = onboardingServicio.registrarPerfil(dto);
        if (usuario == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(modelMapper.map(usuario, UsuarioDTO.class));
    }

    @PutMapping("/{idUsuario}/objetivo")
    public ResponseEntity<?> seleccionarObjetivo(
            @PathVariable Long idUsuario,
            @Valid
            @RequestBody SeleccionObjetivoDTO dto) {

        Usuario usuario = usuarioServicio.seleccionarObjetivo(idUsuario, dto.getIdObjetivo());

        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(Map.of("mensaje", "Objetivo guardado correctamente"));
    }
}
