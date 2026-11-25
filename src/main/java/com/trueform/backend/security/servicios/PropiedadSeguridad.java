package com.trueform.backend.security.servicios;

import com.trueform.backend.dto.*;
import com.trueform.backend.repositorios.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Comprueba la propiedad real en PostgreSQL usando la identidad del JWT.
 * Nunca autoriza a partir de un idUsuario arbitrario enviado por el cliente.
 * Ante identificadores ausentes o registros inexistentes, deniega el acceso.
 */
@Component("propiedadSeguridad")
public class PropiedadSeguridad {

    private final UsuarioRepositorio usuarios;
    private final RutinaRepositorio rutinas;
    private final RutinaEjercicioRepositorio rutinaEjercicios;
    private final ProgresoRepositorio progresos;
    private final FavoritoRepositorio favoritos;
    private final EntrenamientoProgramadoRepositorio programaciones;
    private final RegistroEntrenamientoRepositorio registros;

    public PropiedadSeguridad(
            UsuarioRepositorio usuarios,
            RutinaRepositorio rutinas,
            RutinaEjercicioRepositorio rutinaEjercicios,
            ProgresoRepositorio progresos,
            FavoritoRepositorio favoritos,
            EntrenamientoProgramadoRepositorio programaciones,
            RegistroEntrenamientoRepositorio registros) {
        this.usuarios = usuarios;
        this.rutinas = rutinas;
        this.rutinaEjercicios = rutinaEjercicios;
        this.progresos = progresos;
        this.favoritos = favoritos;
        this.programaciones = programaciones;
        this.registros = registros;
    }

    public boolean esUsuarioActual(Long idUsuario, Authentication autenticacion) {
        if (idUsuario == null || autenticacion == null || !autenticacion.isAuthenticated()) {
            return false;
        }
        String email = autenticacion.getName();
        if (email == null || email.isBlank()) return false;
        return usuarios.findByEmailIgnoreCase(email)
                .map(usuario -> idUsuario.equals(usuario.getIdUsuario()))
                .orElse(false);
    }

    public boolean esRutinaActual(Long idRutina, Authentication autenticacion) {
        if (idRutina == null) return false;
        return rutinas.findById(idRutina)
                .map(r -> r.getUsuario() != null && esUsuarioActual(r.getUsuario().getIdUsuario(), autenticacion))
                .orElse(false);
    }

    public boolean esRutinaEjercicioActual(Long id, Authentication autenticacion) {
        if (id == null) return false;
        return rutinaEjercicios.findById(id)
                .map(re -> re.getRutina() != null && esRutinaActual(re.getRutina().getIdRutina(), autenticacion))
                .orElse(false);
    }

    public boolean esProgresoActual(Long id, Authentication autenticacion) {
        if (id == null) return false;
        return progresos.findById(id)
                .map(p -> p.getUsuario() != null
                        && esUsuarioActual(p.getUsuario().getIdUsuario(), autenticacion)
                        && p.getRutina() != null && esRutinaActual(p.getRutina().getIdRutina(), autenticacion))
                .orElse(false);
    }

    public boolean esFavoritoActual(Long id, Authentication autenticacion) {
        if (id == null) return false;
        return favoritos.findById(id)
                .map(f -> f.getUsuario() != null && esUsuarioActual(f.getUsuario().getIdUsuario(), autenticacion))
                .orElse(false);
    }

    public boolean esProgramacionActual(Long id, Authentication autenticacion) {
        if (id == null) return false;
        return programaciones.findById(id)
                .map(p -> p.getRutina() != null && esRutinaActual(p.getRutina().getIdRutina(), autenticacion))
                .orElse(false);
    }

    public boolean esRegistroActual(Long id, Authentication autenticacion) {
        if (id == null) return false;
        return registros.findById(id)
                .map(r -> r.getRutina() != null && esRutinaActual(r.getRutina().getIdRutina(), autenticacion))
                .orElse(false);
    }

    public boolean puedeCrearRutina(RutinaDTO dto, Authentication a) {
        return dto != null && esUsuarioActual(dto.getIdUsuario(), a);
    }
    public boolean puedeModificarRutina(RutinaDTO dto, Authentication a) {
        return puedeCrearRutina(dto, a) && esRutinaActual(dto.getIdRutina(), a);
    }
    public boolean puedeCrearRutinaEjercicio(RutinaEjercicioDTO dto, Authentication a) {
        return dto != null && esRutinaActual(dto.getIdRutina(), a);
    }
    public boolean puedeModificarRutinaEjercicio(RutinaEjercicioDTO dto, Authentication a) {
        return puedeCrearRutinaEjercicio(dto, a) && esRutinaEjercicioActual(dto.getIdRutinaEjercicio(), a);
    }
    public boolean puedeCrearProgreso(ProgresoDTO dto, Authentication a) {
        return dto != null && esUsuarioActual(dto.getIdUsuario(), a) && esRutinaActual(dto.getIdRutina(), a);
    }
    public boolean puedeModificarProgreso(ProgresoDTO dto, Authentication a) {
        return puedeCrearProgreso(dto, a) && esProgresoActual(dto.getIdProgreso(), a);
    }
    public boolean puedeFinalizarRutina(Long idUsuario, Long idRutina, Authentication a) {
        return esUsuarioActual(idUsuario, a) && esRutinaActual(idRutina, a);
    }
    public boolean puedeCrearFavorito(FavoritoDTO dto, Authentication a) {
        return dto != null && esUsuarioActual(dto.getIdUsuario(), a);
    }
    public boolean puedeCrearProgramacion(EntrenamientoProgramadoDTO dto, Authentication a) {
        return dto != null && esRutinaActual(dto.getIdRutina(), a);
    }
    public boolean puedeModificarProgramacion(EntrenamientoProgramadoDTO dto, Authentication a) {
        return puedeCrearProgramacion(dto, a) && esProgramacionActual(dto.getIdProgramacion(), a);
    }
    public boolean puedeCrearRegistro(RegistroEntrenamientoDTO dto, Authentication a) {
        return dto != null && esRutinaActual(dto.getIdRutina(), a);
    }
    public boolean puedeModificarRegistro(RegistroEntrenamientoDTO dto, Authentication a) {
        return puedeCrearRegistro(dto, a) && esRegistroActual(dto.getIdRegistro(), a);
    }
    public boolean puedeModificarUsuario(ActualizarUsuarioDTO dto, Authentication a) {
        return dto != null && esUsuarioActual(dto.getIdUsuario(), a);
    }
}
