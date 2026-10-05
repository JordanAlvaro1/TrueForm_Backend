package com.trueform.backend.servicios;

import com.trueform.backend.dto.EjercicioGuiadoDTO;
import com.trueform.backend.entidades.RutinaEjercicio;
import com.trueform.backend.repositorios.RutinaEjercicioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RutinaEjercicioServicio {
    @Autowired
    private AccesoServicio acceso;
    @Autowired
    private
    RutinaEjercicioRepositorio rutinaEjercicioRepositorio;

    public List<RutinaEjercicio> listar() {
        return rutinaEjercicioRepositorio.findByRutina_Usuario_IdUsuario(acceso.idActual());
    }

    public RutinaEjercicio buscarPorId(Long id) {
        RutinaEjercicio encontrado = rutinaEjercicioRepositorio.findById(id).orElse(null);
        if (encontrado != null) acceso.verificarUsuario(encontrado.getRutina().getUsuario().getIdUsuario());
        return encontrado;
    }

    public RutinaEjercicio registrar(RutinaEjercicio rutinaEjercicio) {
        acceso.verificarUsuario(rutinaEjercicio.getRutina().getUsuario().getIdUsuario());
        return rutinaEjercicioRepositorio.save(rutinaEjercicio);
    }

    public RutinaEjercicio modificar(RutinaEjercicio rutinaEjercicio) {
        acceso.verificarUsuario(rutinaEjercicio.getRutina().getUsuario().getIdUsuario());
        buscarPorId(rutinaEjercicio.getIdRutinaEjercicio());
        return rutinaEjercicioRepositorio.save(rutinaEjercicio);
    }

    public void eliminar(Long id) {
        buscarPorId(id);
        rutinaEjercicioRepositorio.deleteById(id);
    }

    public List<RutinaEjercicio> buscarPorRutina(Long idRutina) {
        acceso.verificarRutina(idRutina);
        return rutinaEjercicioRepositorio.findByRutina_IdRutina(idRutina);
    }


    // HU8
    public EjercicioGuiadoDTO obtenerEjercicioGuiado(
            Long idRutina,
            String diaSemana,
            Integer posicion) {
        acceso.verificarRutina(idRutina);

        List<RutinaEjercicio> ejercicios =
                rutinaEjercicioRepositorio
                        .findByRutina_IdRutinaAndDiaSemanaIgnoreCaseOrderByOrdenEjercicioAsc(
                                idRutina,
                                diaSemana
                        );

        // No existen ejercicios para ese día
        if (ejercicios.isEmpty()) {
            return null;
        }

        // Posición inválida
        if (posicion < 0 || posicion >= ejercicios.size()) {
            return null;
        }

        RutinaEjercicio rutinaEjercicio = ejercicios.get(posicion);

        boolean ultimoEjercicio =
                posicion == ejercicios.size() - 1;

        String mensaje;

        if (ultimoEjercicio) {
            mensaje = "Último ejercicio de la rutina.";
        } else {
            mensaje = "Ejercicio " + (posicion + 1)
                    + " de " + ejercicios.size();
        }

        return new EjercicioGuiadoDTO(
                rutinaEjercicio.getIdRutinaEjercicio(),
                rutinaEjercicio.getEjercicio().getIdEjercicio(),
                rutinaEjercicio.getEjercicio().getNombreEjercicio(),
                rutinaEjercicio.getEjercicio().getDescripcion(),
                rutinaEjercicio.getEjercicio().getGrupoMuscular(),
                rutinaEjercicio.getSeries(),
                rutinaEjercicio.getRepeticiones(),
                rutinaEjercicio.getDescansoSegundos(),
                rutinaEjercicio.getDiaSemana(),
                rutinaEjercicio.getOrdenEjercicio(),
                ultimoEjercicio,
                mensaje
        );
    }

}

