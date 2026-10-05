package com.trueform.backend.servicios;

import com.trueform.backend.entidades.Objetivo;
import com.trueform.backend.entidades.Usuario;
import com.trueform.backend.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.trueform.backend.dto.RecomendacionInicialDTO;



import java.util.List;
import java.util.Optional;
@Service
public class UsuarioServicio {
    @Autowired
    private AccesoServicio acceso;
    @Autowired
    private
    UsuarioRepositorio usuarioRepositorio;

    public List<Usuario> listar() {
        return java.util.List.of(usuarioRepositorio.findById(acceso.idActual()).orElseThrow());
    }

    public Usuario buscarPorId(Long id) {
        Usuario encontrado = usuarioRepositorio.findById(id).orElse(null);
        if (encontrado != null) acceso.verificarUsuario(encontrado.getIdUsuario());
        return encontrado;
    }

    public Usuario registrar(Usuario usuario) {
        usuario.setRol("USER");
        return usuarioRepositorio.save(usuario);
    }

    public Usuario modificar(Usuario usuario) {
        acceso.verificarUsuario(usuario.getIdUsuario());
        buscarPorId(usuario.getIdUsuario());
        return usuarioRepositorio.save(usuario);
    }

    public void eliminar(Long id) {
        buscarPorId(id);
        usuarioRepositorio.deleteById(id);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepositorio.findByEmailIgnoreCase(email.trim());
    }

    @Autowired
    private ObjetivoServicio objetivoServicio;

    public Usuario seleccionarObjetivo(Long idUsuario, Long idObjetivo) {
        acceso.verificarUsuario(idUsuario);
        Usuario usuario = usuarioRepositorio.findById(idUsuario).orElse(null);
        Objetivo objetivo = objetivoServicio.buscarPorId(idObjetivo);

        if (usuario == null || objetivo == null) {
            return null;
        }

        usuario.setObjetivo(objetivo);
        return usuarioRepositorio.save(usuario);
    }
    // HU5 - Obtener recomendaciones iniciales según nivel y objetivo
    public RecomendacionInicialDTO obtenerRecomendacionInicial(Long idUsuario) {
        acceso.verificarUsuario(idUsuario);

        Usuario usuario = usuarioRepositorio.findById(idUsuario).orElse(null);

        if (usuario == null) {
            return null;
        }

        // Validar que el perfil tenga la información necesaria
        if (usuario.getNivel() == null ||
                usuario.getNivel().isBlank() ||
                usuario.getObjetivo() == null) {

            return new RecomendacionInicialDTO(
                    usuario.getIdUsuario(),
                    usuario.getNivel(),
                    null,
                    null,
                    "Completa los datos de tu perfil para recibir recomendaciones personalizadas."
            );
        }

        String nivel = usuario.getNivel();
        String objetivo = usuario.getObjetivo().getNombreObjetivo();

        String recomendacion;

        if (nivel.equalsIgnoreCase("principiante")) {

            recomendacion =
                    "Comienza con ejercicios básicos, prioriza la técnica y aumenta la intensidad progresivamente.";

        } else if (nivel.equalsIgnoreCase("intermedio")) {

            recomendacion =
                    "Incrementa progresivamente la carga y controla el volumen de entrenamiento.";

        } else if (nivel.equalsIgnoreCase("avanzado")) {

            recomendacion =
                    "Utiliza una planificación avanzada de intensidad, volumen y recuperación.";

        } else {

            recomendacion =
                    "Mantén una rutina constante y adapta la intensidad según tu progreso.";
        }

        // Personalizar también según el objetivo
        if (objetivo.equalsIgnoreCase("Ganar fuerza")) {

            recomendacion +=
                    " Para tu objetivo de ganar fuerza, prioriza ejercicios compuestos y una progresión gradual de cargas.";

        } else if (objetivo.equalsIgnoreCase("Perder grasa")) {

            recomendacion +=
                    " Para tu objetivo de perder grasa, combina entrenamiento de fuerza con actividad cardiovascular.";

        } else if (objetivo.equalsIgnoreCase("Mejorar condición física")) {

            recomendacion +=
                    " Para mejorar tu condición física, combina ejercicios de fuerza, resistencia y movilidad.";
        }

        return new RecomendacionInicialDTO(
                usuario.getIdUsuario(),
                nivel,
                objetivo,
                recomendacion,
                "Recomendación generada correctamente."
        );
    }
}
