
package com.trueform.backend.security.servicios;

import com.trueform.backend.entidades.Usuario;
import com.trueform.backend.repositorios.UsuarioRepositorio;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepositorio usuarioRepositorio;

    public CustomUserDetailsService(
            UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        if (email == null || email.isBlank()) {
            throw new UsernameNotFoundException(
                    "Credenciales incorrectas"
            );
        }

        Usuario usuario = usuarioRepositorio
                .findByEmailIgnoreCase(email.trim())
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Credenciales incorrectas"
                        )
                );

        // Por defecto, los usuarios son deportistas.
        String rol = "ADMIN".equalsIgnoreCase(usuario.getRol())
                ? "ADMIN"
                : "USER";

        return User.withUsername(usuario.getEmail())
                .password(usuario.getPasswordHash())
                .authorities("ROLE_" + rol)
                .build();
    }
}
