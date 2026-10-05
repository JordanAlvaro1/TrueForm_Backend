package com.trueform.backend.controladores;

import com.trueform.backend.dto.LoginDTO;
import com.trueform.backend.dto.UsuarioDTO;
import com.trueform.backend.entidades.Usuario;
import com.trueform.backend.servicios.UsuarioServicio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/login")
    public ResponseEntity<UsuarioDTO> login(
            @RequestBody LoginDTO loginDTO) {
        if (loginDTO.getEmail() == null || loginDTO.getEmail().isBlank() || loginDTO.getPassword() == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Usuario> usuarioOptional =
                usuarioServicio.buscarPorEmail(loginDTO.getEmail());

        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.status(401).build();
        }

        Usuario usuario = usuarioOptional.get();

        if (!passwordEncoder.matches(
                loginDTO.getPassword(),
                usuario.getPasswordHash())) {

            return ResponseEntity.status(401).build();
        }

        UsuarioDTO usuarioDTO =
                modelMapper.map(usuario, UsuarioDTO.class);

        return ResponseEntity.ok(usuarioDTO);
    }
}
