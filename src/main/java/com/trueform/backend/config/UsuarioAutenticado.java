package com.trueform.backend.config;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.List;

// Conserva el ID: cambiar el correo no debe cambiar la identidad del usuario.
public class UsuarioAutenticado extends User {
    private final Long idUsuario;

    public UsuarioAutenticado(Long idUsuario, String email, String passwordHash, String rol) {
        super(email, passwordHash, List.of(new SimpleGrantedAuthority("ROLE_" + rol)));
        this.idUsuario = idUsuario;
    }

    public Long getIdUsuario() { return idUsuario; }
}
