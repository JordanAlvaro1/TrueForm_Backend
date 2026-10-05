package com.trueform.backend.servicios;

import com.trueform.backend.entidades.RegistroEntrenamiento;
import com.trueform.backend.repositorios.RegistroEntrenamientoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RegistroEntrenamientoServicio {
    @Autowired
    private AccesoServicio acceso;
    @Autowired
    private
    RegistroEntrenamientoRepositorio repositorio;

    public RegistroEntrenamiento buscarPorId(Long id) {
        RegistroEntrenamiento encontrado = repositorio.findById(id).orElse(null);
        if (encontrado != null) acceso.verificarUsuario(encontrado.getRutina().getUsuario().getIdUsuario());
        return encontrado;
    }
    public RegistroEntrenamiento registrar(RegistroEntrenamiento entidad) {
        acceso.verificarUsuario(entidad.getRutina().getUsuario().getIdUsuario());
        return repositorio.save(entidad);
    }
    public RegistroEntrenamiento modificar(RegistroEntrenamiento entidad) {
        acceso.verificarUsuario(entidad.getRutina().getUsuario().getIdUsuario());
        buscarPorId(entidad.getIdRegistro());
        return repositorio.save(entidad);
    }
    public void eliminar(Long id) {
        buscarPorId(id);
        repositorio.deleteById(id);
    }
    public List<RegistroEntrenamiento> buscarPorUsuario(Long idUsuario) {
        acceso.verificarUsuario(idUsuario);
        return repositorio.findByRutina_Usuario_IdUsuarioOrderByFechaAsc(idUsuario);
    }
    public List<RegistroEntrenamiento> buscarPorUsuarioYEjercicio(Long idUsuario, Long idEjercicio) {
        acceso.verificarUsuario(idUsuario);
        return repositorio.findByRutina_Usuario_IdUsuarioAndEjercicio_IdEjercicioOrderByFechaAsc(idUsuario, idEjercicio);
    }
}
