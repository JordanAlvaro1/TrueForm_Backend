package com.trueform.backend.repositorios;

import com.trueform.backend.entidades.Objetivo;
import com.trueform.backend.entidades.RutinaEjercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjetivoRepositorio extends JpaRepository<Objetivo, Long> {
}
