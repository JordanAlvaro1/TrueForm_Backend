package com.trueform.backend.repositorios;

import com.trueform.backend.entidades.TipContenido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipContenidoRepositorio extends JpaRepository<TipContenido, Long> {

    List<TipContenido> findByActivoTrueOrderByOrdenVisualizacionAscIdTipAsc();

    List<TipContenido> findAllByOrderByOrdenVisualizacionAscIdTipAsc();

    Optional<TipContenido> findByTituloIgnoreCase(String titulo);
}
