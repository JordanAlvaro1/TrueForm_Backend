package com.trueform.backend.servicios;

import com.trueform.backend.config.UsuarioAutenticado;
import com.trueform.backend.entidades.Rutina;
import com.trueform.backend.repositorios.RutinaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AccesoServicio {
    @Autowired
    private RutinaRepositorio rutinas;

    public Long idActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof UsuarioAutenticado)) {
            throw new AccessDeniedException("Debes autenticarte");
        }
        UsuarioAutenticado usuario = (UsuarioAutenticado) auth.getPrincipal();
        return usuario.getIdUsuario();
    }

    public void verificarUsuario(Long idUsuario) {
        if (!idActual().equals(idUsuario)) {
            throw new AccessDeniedException("Solo puedes acceder a tus propios datos");
        }
    }

    public void verificarRutina(Long idRutina) {
        Rutina rutina = rutinas.findById(idRutina).orElse(null);
        if (rutina != null) verificarUsuario(rutina.getUsuario().getIdUsuario());
    }
}
