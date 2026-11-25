
package com.trueform.backend.repositorios;

import com.trueform.backend.entidades.VideoEducativo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoEducativoRepositorio
        extends JpaRepository<VideoEducativo, Long> {

    List<VideoEducativo> findByActivoTrueOrderByTituloAsc();

    List<VideoEducativo> findByActivoTrueAndNivelIgnoreCase(
            String nivel
    );
}
