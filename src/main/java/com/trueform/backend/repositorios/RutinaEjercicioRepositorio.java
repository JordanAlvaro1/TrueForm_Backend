package com.trueform.backend.repositorios;

import com.trueform.backend.entidades.RutinaEjercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RutinaEjercicioRepositorio extends JpaRepository<RutinaEjercicio, Long> {

    List<RutinaEjercicio> findByRutina_IdRutina(Long idRutina);

    List<RutinaEjercicio>
    findByRutina_IdRutinaOrderByOrdenEjercicioAsc(Long idRutina);

    // HU8 - Obtener ejercicios de una rutina por día y en orden
    List<RutinaEjercicio>
    findByRutina_IdRutinaAndDiaSemanaIgnoreCaseOrderByOrdenEjercicioAsc(
            Long idRutina,
            String diaSemana
    );
    List<RutinaEjercicio> findByRutina_Usuario_IdUsuario(Long idUsuario);
}
