package com.trueform.backend.servicios;

import com.trueform.backend.entidades.Favorito;
import com.trueform.backend.repositorios.FavoritoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FavoritoServicio {
    @Autowired private AccesoServicio acceso;
    @Autowired
    private FavoritoRepositorio repositorio;

    public Favorito buscarPorId(Long id) {
        Favorito encontrado = repositorio.findById(id).orElse(null);
        if (encontrado != null) acceso.verificarUsuario(encontrado.getUsuario().getIdUsuario());
        return encontrado;
    }
    public Favorito registrar(Favorito entidad) {
        acceso.verificarUsuario(entidad.getUsuario().getIdUsuario());
        return repositorio.save(entidad);
    }
    public Favorito modificar(Favorito entidad) {
        acceso.verificarUsuario(entidad.getUsuario().getIdUsuario());
        buscarPorId(entidad.getIdFavorito());
        return repositorio.save(entidad);
    }
    public void eliminar(Long id) {
        buscarPorId(id);
        repositorio.deleteById(id);
    }
    public List<Favorito> buscarPorUsuario(Long idUsuario) {
        acceso.verificarUsuario(idUsuario);
        return repositorio.findByUsuario_IdUsuario(idUsuario);
    }
    public Favorito buscarRepetido(Long idUsuario, Long idEjercicio) {
        acceso.verificarUsuario(idUsuario);
        return repositorio.findByUsuario_IdUsuarioAndEjercicio_IdEjercicio(idUsuario, idEjercicio).orElse(null);
    }
}
