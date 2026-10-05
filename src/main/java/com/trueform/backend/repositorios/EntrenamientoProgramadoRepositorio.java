package com.trueform.backend.repositorios;

import com.trueform.backend.entidades.EntrenamientoProgramado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EntrenamientoProgramadoRepositorio extends JpaRepository<EntrenamientoProgramado, Long> {
    List<EntrenamientoProgramado> findByRutina_Usuario_IdUsuarioOrderByFechaAsc(Long idUsuario);
}
