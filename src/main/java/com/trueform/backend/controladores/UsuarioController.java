package com.trueform.backend.controladores;

import com.trueform.backend.dto.RecomendacionInicialDTO;
import com.trueform.backend.dto.RegistroUsuarioDTO;
import com.trueform.backend.dto.UsuarioDTO;
import com.trueform.backend.entidades.Objetivo;
import com.trueform.backend.entidades.Usuario;
import com.trueform.backend.servicios.ObjetivoServicio;
import com.trueform.backend.servicios.UsuarioServicio;
import com.trueform.backend.dto.ActualizarUsuarioDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ObjetivoServicio objetivoServicio;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public List<UsuarioDTO> listar() {

        return usuarioServicio.listar()
                .stream()
                .map(usuario ->
                        modelMapper.map(usuario, UsuarioDTO.class))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Long id) {

        Usuario usuario = usuarioServicio.buscarPorId(id);

        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        UsuarioDTO usuarioDTO =
                modelMapper.map(usuario, UsuarioDTO.class);

        return ResponseEntity.ok(usuarioDTO);
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> registrar(
            @Valid @RequestBody RegistroUsuarioDTO registroUsuarioDTO) {
        if (registroUsuarioDTO.getNombres() == null ||
            registroUsuarioDTO.getApellidos() == null ||
            registroUsuarioDTO.getEmail() == null ||
            registroUsuarioDTO.getPassword() == null ||
            registroUsuarioDTO.getEdad() == null ||
            registroUsuarioDTO.getNivel() == null ||
            registroUsuarioDTO.getIdObjetivo() == null) return ResponseEntity.badRequest().build();

        if (registroUsuarioDTO.getNombres().isBlank() || registroUsuarioDTO.getNombres().length() > 100 ||
            registroUsuarioDTO.getApellidos().isBlank() || registroUsuarioDTO.getApellidos().length() > 100 ||
            registroUsuarioDTO.getEmail().length() > 150 ||
            registroUsuarioDTO.getEdad() <= 0 || registroUsuarioDTO.getEdad() > 120 ||
            (!registroUsuarioDTO.getNivel().equals("principiante") && !registroUsuarioDTO.getNivel().equals("intermedio") && !registroUsuarioDTO.getNivel().equals("avanzado"))) {
            return ResponseEntity.badRequest().build();
        }
        if ((registroUsuarioDTO.getPeso() != null && (registroUsuarioDTO.getPeso().signum() <= 0 ||
            registroUsuarioDTO.getPeso().compareTo(new java.math.BigDecimal("999.99")) > 0)) ||
            (registroUsuarioDTO.getAltura() != null && (registroUsuarioDTO.getAltura().signum() <= 0 ||
            registroUsuarioDTO.getAltura().compareTo(new java.math.BigDecimal("999.99")) > 0))) {
            return ResponseEntity.badRequest().build();
        }
        registroUsuarioDTO.setEmail(registroUsuarioDTO.getEmail().trim().toLowerCase(java.util.Locale.ROOT));
        Usuario repetido = usuarioServicio.buscarPorEmail(registroUsuarioDTO.getEmail()).orElse(null);
        if (repetido != null) return ResponseEntity.status(409).build();
        if (registroUsuarioDTO.getPassword().length() < 8 ||
            registroUsuarioDTO.getPassword().getBytes(java.nio.charset.StandardCharsets.UTF_8).length > 72) {
            return ResponseEntity.badRequest().build();
        }


        Objetivo objetivo =
                objetivoServicio.buscarPorId(registroUsuarioDTO.getIdObjetivo());

        if (objetivo == null) {
            return ResponseEntity.badRequest().build();
        }

        Usuario usuario = new Usuario();

        usuario.setNombres(registroUsuarioDTO.getNombres());
        usuario.setApellidos(registroUsuarioDTO.getApellidos());
        usuario.setEmail(registroUsuarioDTO.getEmail());

        usuario.setPasswordHash(
                passwordEncoder.encode(registroUsuarioDTO.getPassword())
        );

        usuario.setEdad(registroUsuarioDTO.getEdad());
        usuario.setPeso(registroUsuarioDTO.getPeso());
        usuario.setAltura(registroUsuarioDTO.getAltura());
        usuario.setNivel(registroUsuarioDTO.getNivel());
        usuario.setObjetivo(objetivo);
        usuario.setEquipo(registroUsuarioDTO.getEquipo());

        Usuario usuarioRegistrado;
        try {
            usuarioRegistrado = usuarioServicio.registrar(usuario);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            return ResponseEntity.status(409).build();
        }

        UsuarioDTO usuarioDTO =
                modelMapper.map(usuarioRegistrado, UsuarioDTO.class);

        return ResponseEntity.ok(usuarioDTO);
    }

    @PutMapping
    public ResponseEntity<UsuarioDTO> modificar(
            @Valid @RequestBody ActualizarUsuarioDTO actualizarUsuarioDTO) {
        if (actualizarUsuarioDTO.getIdUsuario() == null ||
            actualizarUsuarioDTO.getNombres() == null ||
            actualizarUsuarioDTO.getApellidos() == null ||
            actualizarUsuarioDTO.getEmail() == null ||
            actualizarUsuarioDTO.getEdad() == null ||
            actualizarUsuarioDTO.getNivel() == null ||
            actualizarUsuarioDTO.getIdObjetivo() == null) return ResponseEntity.badRequest().build();

        if (actualizarUsuarioDTO.getNombres().isBlank() || actualizarUsuarioDTO.getNombres().length() > 100 ||
            actualizarUsuarioDTO.getApellidos().isBlank() || actualizarUsuarioDTO.getApellidos().length() > 100 ||
            actualizarUsuarioDTO.getEmail().length() > 150 ||
            actualizarUsuarioDTO.getEdad() <= 0 || actualizarUsuarioDTO.getEdad() > 120 ||
            (!actualizarUsuarioDTO.getNivel().equals("principiante") && !actualizarUsuarioDTO.getNivel().equals("intermedio") && !actualizarUsuarioDTO.getNivel().equals("avanzado"))) {
            return ResponseEntity.badRequest().build();
        }
        if ((actualizarUsuarioDTO.getPeso() != null && (actualizarUsuarioDTO.getPeso().signum() <= 0 ||
            actualizarUsuarioDTO.getPeso().compareTo(new java.math.BigDecimal("999.99")) > 0)) ||
            (actualizarUsuarioDTO.getAltura() != null && (actualizarUsuarioDTO.getAltura().signum() <= 0 ||
            actualizarUsuarioDTO.getAltura().compareTo(new java.math.BigDecimal("999.99")) > 0))) {
            return ResponseEntity.badRequest().build();
        }
        actualizarUsuarioDTO.setEmail(actualizarUsuarioDTO.getEmail().trim().toLowerCase(java.util.Locale.ROOT));
        Usuario repetido = usuarioServicio.buscarPorEmail(actualizarUsuarioDTO.getEmail()).orElse(null);
        if (repetido != null && !repetido.getIdUsuario().equals(actualizarUsuarioDTO.getIdUsuario())) return ResponseEntity.status(409).build();


        Usuario usuario =
                usuarioServicio.buscarPorId(actualizarUsuarioDTO.getIdUsuario());

        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        Objetivo objetivo =
                objetivoServicio.buscarPorId(actualizarUsuarioDTO.getIdObjetivo());

        if (objetivo == null) {
            return ResponseEntity.badRequest().build();
        }

        usuario.setNombres(actualizarUsuarioDTO.getNombres());
        usuario.setApellidos(actualizarUsuarioDTO.getApellidos());
        usuario.setEmail(actualizarUsuarioDTO.getEmail());
        usuario.setEdad(actualizarUsuarioDTO.getEdad());
        usuario.setPeso(actualizarUsuarioDTO.getPeso());
        usuario.setAltura(actualizarUsuarioDTO.getAltura());
        usuario.setNivel(actualizarUsuarioDTO.getNivel());
        usuario.setObjetivo(objetivo);

        Usuario usuarioModificado;
        try {
            usuarioModificado = usuarioServicio.modificar(usuario);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            return ResponseEntity.status(409).build();
        }

        UsuarioDTO usuarioDTO =
                modelMapper.map(usuarioModificado, UsuarioDTO.class);

        return ResponseEntity.ok(usuarioDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        Usuario usuario = usuarioServicio.buscarPorId(id);

        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            usuarioServicio.eliminar(id);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            return ResponseEntity.status(409).build();
        }

        return ResponseEntity.noContent().build();
    }
    // HU5 - Visualizar recomendaciones iniciales en Home
    @GetMapping("/{idUsuario}/recomendaciones")
    public ResponseEntity<RecomendacionInicialDTO> obtenerRecomendaciones(
            @PathVariable Long idUsuario) {

        RecomendacionInicialDTO recomendacion =
                usuarioServicio.obtenerRecomendacionInicial(idUsuario);

        if (recomendacion == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(recomendacion);
    }
}
