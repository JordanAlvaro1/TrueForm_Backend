package com.trueform.backend.servicios;

import com.trueform.backend.dto.GlosarioTerminoRequestDTO;
import com.trueform.backend.entidades.GlosarioTermino;
import com.trueform.backend.repositorios.GlosarioTerminoRepositorio;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class GlosarioAdministracionServicio {

    private final GlosarioTerminoRepositorio repositorio;

    public GlosarioAdministracionServicio(GlosarioTerminoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Transactional(readOnly = true)
    public List<GlosarioTermino> listar() {
        return repositorio.findAll(Sort.by("termino").ascending());
    }

    @Transactional
    public GlosarioTermino crear(GlosarioTerminoRequestDTO datos) {
        String termino = datos.termino().trim();
        comprobarDuplicado(termino, null);

        GlosarioTermino nuevo = new GlosarioTermino();
        nuevo.setTermino(termino);
        nuevo.setDefinicion(datos.definicion().trim());
        nuevo.setCategoria(datos.categoria().trim());
        nuevo.setActivo(true);
        return repositorio.save(nuevo);
    }

    @Transactional
    public GlosarioTermino actualizar(Long id, GlosarioTerminoRequestDTO datos) {
        GlosarioTermino existente = buscar(id);
        String termino = datos.termino().trim();
        comprobarDuplicado(termino, id);

        existente.setTermino(termino);
        existente.setDefinicion(datos.definicion().trim());
        existente.setCategoria(datos.categoria().trim());
        // Conservar el estado publicado/desactivado al editar.
        return repositorio.save(existente);
    }

    @Transactional
    public void desactivar(Long id) {
        GlosarioTermino existente = buscar(id);
        existente.setActivo(false);
        repositorio.save(existente);
    }

    @Transactional
    public GlosarioTermino reactivar(Long id) {
        GlosarioTermino existente = buscar(id);
        existente.setActivo(true);
        return repositorio.save(existente);
    }

    private GlosarioTermino buscar(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Termino del glosario no encontrado"));
    }

    private void comprobarDuplicado(String termino, Long idActual) {
        repositorio.findByTerminoIgnoreCase(termino).ifPresent(encontrado -> {
            if (idActual == null || !encontrado.getIdTermino().equals(idActual)) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT, "El termino ya existe en el glosario");
            }
        });
    }
}
