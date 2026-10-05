package com.trueform.backend.repositorios;

import com.trueform.backend.entidades.Ejercicio;
import com.trueform.backend.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EjercicioRepositorio extends JpaRepository<Ejercicio,Long> {
    List<Ejercicio> findByGrupoMuscular(String grupoMuscular);
}
