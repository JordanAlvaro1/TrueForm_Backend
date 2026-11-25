package com.trueform.backend.servicios;

import com.trueform.backend.dto.TipContenidoRequestDTO;
import com.trueform.backend.entidades.TipContenido;
import com.trueform.backend.repositorios.TipContenidoRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TipAdministracionServicio {

    private final TipContenidoRepositorio repositorio;

    public TipAdministracionServicio(TipContenidoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Transactional(readOnly = true)
    public List<TipContenido> listar() {
        // Se incluyen también los tips desactivados: el administrador debe poder reactivarlos.
        return repositorio.findAllByOrderByOrdenVisualizacionAscIdTipAsc();
    }

    @Transactional
    public TipContenido crear(TipContenidoRequestDTO datos) {
        String titulo = datos.titulo().trim();
        comprobarDuplicado(titulo, null);

        TipContenido tip = new TipContenido();
        tip.setCategoria(datos.categoria().trim());
        tip.setTitulo(titulo);
        tip.setContenido(datos.contenido().trim());
        tip.setOrdenVisualizacion(datos.ordenVisualizacion());
        tip.setActivo(true);
        return repositorio.save(tip);
    }

    @Transactional
    public TipContenido actualizar(Long id, TipContenidoRequestDTO datos) {
        TipContenido tip = buscar(id);
        String titulo = datos.titulo().trim();
        comprobarDuplicado(titulo, id);

        tip.setCategoria(datos.categoria().trim());
        tip.setTitulo(titulo);
        tip.setContenido(datos.contenido().trim());
        tip.setOrdenVisualizacion(datos.ordenVisualizacion());
        // Conservar el estado actual: editar no reactiva un consejo desactivado.
        return repositorio.save(tip);
    }

    @Transactional
    public void desactivar(Long id) {
        TipContenido tip = buscar(id);
        tip.setActivo(false);
        repositorio.save(tip);
    }

    @Transactional
    public TipContenido reactivar(Long id) {
        TipContenido tip = buscar(id);
        tip.setActivo(true);
        return repositorio.save(tip);
    }

    private TipContenido buscar(Long id) {
        return repositorio.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Consejo no encontrado"));
    }

    private void comprobarDuplicado(String titulo, Long idActual) {
        repositorio.findByTituloIgnoreCase(titulo).ifPresent(encontrado -> {
            if (idActual == null || !encontrado.getIdTip().equals(idActual)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Ya existe un consejo con ese título");
            }
        });
    }
}
