package com.trueform.backend.repositorios;

import com.trueform.backend.entidades.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritoRepositorio extends JpaRepository<Favorito, Long> {
    List<Favorito> findByUsuario_IdUsuario(Long idUsuario);
    Optional<Favorito> findByUsuario_IdUsuarioAndEjercicio_IdEjercicio(Long idUsuario, Long idEjercicio);
}
