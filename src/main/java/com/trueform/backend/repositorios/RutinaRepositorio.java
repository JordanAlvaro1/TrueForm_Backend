package com.trueform.backend.repositorios;

import com.trueform.backend.entidades.Rutina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RutinaRepositorio extends JpaRepository<Rutina, Long> {
    List<Rutina> findByUsuario_IdUsuario(Long idUsuario);
}
