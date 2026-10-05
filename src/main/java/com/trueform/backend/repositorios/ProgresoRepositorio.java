package com.trueform.backend.repositorios;

import com.trueform.backend.entidades.Progreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProgresoRepositorio extends JpaRepository<Progreso, Long> {

    List<Progreso> findByUsuario_IdUsuario(Long idUsuario);

    // HU10 - Obtener progreso del usuario dentro de un rango de fechas
    List<Progreso>
    findByUsuario_IdUsuarioAndFechaRegistroBetweenOrderByFechaRegistroAsc(
            Long idUsuario,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin
    );
    // Intervalo [inicio, siguiente mes): no incluye registros del mes siguiente.
    List<Progreso> findByUsuario_IdUsuarioAndFechaRegistroGreaterThanEqualAndFechaRegistroLessThanOrderByFechaRegistroAsc(
            Long idUsuario, LocalDateTime inicio, LocalDateTime finExclusivo);
}
