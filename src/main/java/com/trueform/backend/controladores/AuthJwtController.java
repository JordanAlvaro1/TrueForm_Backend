
package com.trueform.backend.controladores;

import com.trueform.backend.dto.JwtLoginResponseDTO;
import com.trueform.backend.dto.LoginDTO;
import com.trueform.backend.dto.UsuarioDTO;
import com.trueform.backend.entidades.Usuario;
import com.trueform.backend.repositorios.UsuarioRepositorio;
import com.trueform.backend.security.util.JwtUtil;

import org.modelmapper.ModelMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthJwtController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioRepositorio usuarioRepositorio;
    private final ModelMapper modelMapper;

    public AuthJwtController(
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil,
            UsuarioRepositorio usuarioRepositorio,
            ModelMapper modelMapper) {

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usuarioRepositorio = usuarioRepositorio;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/login-jwt")
    public ResponseEntity<?> loginJwt(
            @RequestBody LoginDTO loginDTO) {

        if (loginDTO == null ||
                loginDTO.getEmail() == null ||
                loginDTO.getEmail().isBlank() ||
                loginDTO.getPassword() == null ||
                loginDTO.getPassword().isBlank()) {

            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "mensaje", "Email y contraseña obligatorios"
                    ));
        }

        String email = loginDTO.getEmail().trim();

        try {
            Authentication autenticacion =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    email,
                                    loginDTO.getPassword()
                            )
                    );

            UserDetails userDetails =
                    (UserDetails) autenticacion.getPrincipal();

            String token = jwtUtil.generateToken(userDetails);

            Usuario usuario = usuarioRepositorio
                    .findByEmailIgnoreCase(email)
                    .orElseThrow();

            String rol = userDetails.getAuthorities()
                    .stream()
                    .anyMatch(authority ->
                            authority.getAuthority()
                                    .equals("ROLE_ADMIN"))
                    ? "ADMIN"
                    : "USER";

            UsuarioDTO usuarioDTO =
                    modelMapper.map(usuario, UsuarioDTO.class);

            JwtLoginResponseDTO respuesta =
                    new JwtLoginResponseDTO(
                            token,
                            rol,
                            usuarioDTO
                    );

            return ResponseEntity.ok(respuesta);

        } catch (AuthenticationException exception) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "mensaje", "Credenciales incorrectas"
                    ));
        }
    }

    @GetMapping("/me")
    public Map<String, Object> obtenerUsuarioAutenticado(
            Authentication authentication) {

        return Map.of(
                "email", authentication.getName(),
                "roles", authentication.getAuthorities()
                        .stream()
                        .map(authority -> authority.getAuthority())
                        .toList()
        );
    }


    @GetMapping("/admin-check")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> comprobarAdministrador(
            Authentication authentication) {

        return ResponseEntity.ok(
                Map.of(
                        "mensaje", "Acceso administrativo autorizado",
                        "email", authentication.getName()
                )
        );
    }
}
