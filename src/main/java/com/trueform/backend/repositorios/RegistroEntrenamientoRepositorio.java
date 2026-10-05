package com.trueform.backend.repositorios;

import com.trueform.backend.entidades.RegistroEntrenamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistroEntrenamientoRepositorio extends JpaRepository<RegistroEntrenamiento, Long> {
    List<RegistroEntrenamiento> findByRutina_Usuario_IdUsuarioOrderByFechaAsc(Long idUsuario);
    List<RegistroEntrenamiento> findByRutina_Usuario_IdUsuarioAndEjercicio_IdEjercicioOrderByFechaAsc(Long idUsuario, Long idEjercicio);
}
