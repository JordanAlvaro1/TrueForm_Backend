package com.trueform.backend.servicios;

import com.trueform.backend.entidades.Favorito;
import com.trueform.backend.repositorios.FavoritoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FavoritoServicio {

    @Autowired
    private FavoritoRepositorio repositorio;

    public Favorito buscarPorId(Long id) {
        Favorito encontrado = repositorio.findById(id).orElse(null);

        return encontrado;
    }
    public Favorito registrar(Favorito entidad) {

        return repositorio.save(entidad);
    }
    public Favorito modificar(Favorito entidad) {

        buscarPorId(entidad.getIdFavorito());
        return repositorio.save(entidad);
    }
    public void eliminar(Long id) {
        buscarPorId(id);
        repositorio.deleteById(id);
    }
    public List<Favorito> buscarPorUsuario(Long idUsuario) {

        return repositorio.findByUsuario_IdUsuario(idUsuario);
    }
    public Favorito buscarRepetido(Long idUsuario, Long idEjercicio) {

        return repositorio.findByUsuario_IdUsuarioAndEjercicio_IdEjercicio(idUsuario, idEjercicio).orElse(null);
    }
}
