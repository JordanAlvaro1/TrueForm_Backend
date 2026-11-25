
package com.trueform.backend.repositorios;

import com.trueform.backend.entidades.GlosarioTermino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GlosarioTerminoRepositorio
        extends JpaRepository<GlosarioTermino, Long> {

    Optional<GlosarioTermino> findByTerminoIgnoreCase(
            String termino
    );

    Optional<GlosarioTermino> findByTerminoIgnoreCaseAndActivoTrue(
            String termino
    );
}
