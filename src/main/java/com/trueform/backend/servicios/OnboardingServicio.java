package com.trueform.backend.servicios;

import com.trueform.backend.dto.OnboardingDTO;
import com.trueform.backend.entidades.Objetivo;
import com.trueform.backend.entidades.Usuario;
import com.trueform.backend.repositorios.ObjetivoRepositorio;
import com.trueform.backend.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OnboardingServicio {
    @Autowired
    private AccesoServicio acceso;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private ObjetivoRepositorio objetivoRepositorio;

    public Usuario registrarPerfil(OnboardingDTO dto) {
        acceso.verificarUsuario(dto.getIdUsuario());
        Usuario usuario = usuarioRepositorio.findById(dto.getIdUsuario()).orElse(null);
        Objetivo objetivo = objetivoRepositorio.findById(dto.getIdObjetivo()).orElse(null);
        if (usuario == null || objetivo == null) {
            return null;
        }
        usuario.setNivel(dto.getNivel());
        usuario.setObjetivo(objetivo);
        usuario.setEquipo(String.join(", ", dto.getEquipo()));
        return usuarioRepositorio.save(usuario);
    }
}
