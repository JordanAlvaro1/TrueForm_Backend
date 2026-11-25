package com.trueform.backend.servicios;

import com.trueform.backend.dto.VideoEducativoRequestDTO;
import com.trueform.backend.entidades.VideoEducativo;
import com.trueform.backend.repositorios.VideoEducativoRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class VideoEducativoServicio {

    private static final Set<String> PLATAFORMAS_PERMITIDAS = Set.of(
            "youtube.com", "www.youtube.com", "m.youtube.com",
            "youtu.be", "www.youtu.be",
            "youtube-nocookie.com", "www.youtube-nocookie.com",
            "vimeo.com", "www.vimeo.com", "player.vimeo.com"
    );

    private static final Set<String> NIVELES = Set.of(
            "GENERAL", "PRINCIPIANTE", "INTERMEDIO", "AVANZADO"
    );

    private final VideoEducativoRepositorio repositorio;

    public VideoEducativoServicio(VideoEducativoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Transactional(readOnly = true)
    public List<VideoEducativo> listarTodos() {
        // Incluye inactivos para que ADMIN pueda reactivarlos.
        return repositorio.findAll().stream()
                .sorted((a, b) -> a.getTitulo().compareToIgnoreCase(b.getTitulo()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<VideoEducativo> listarPublicados(String nivel) {
        List<VideoEducativo> publicados =
                repositorio.findByActivoTrueOrderByTituloAsc();

        if (nivel == null || nivel.isBlank()) {
            return publicados;
        }

        String buscado = nivel.trim().toUpperCase(Locale.ROOT);
        if (!NIVELES.contains(buscado)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Nivel no reconocido");
        }

        // Los videos GENERAL son recomendables para todos los niveles.
        return publicados.stream()
                .filter(video -> "GENERAL".equalsIgnoreCase(video.getNivel())
                        || buscado.equalsIgnoreCase(video.getNivel()))
                .toList();
    }

    @Transactional
    public VideoEducativo crear(VideoEducativoRequestDTO datos) {
        String url = validarUrl(datos.urlVideo());
        comprobarUrlDuplicada(url, null);

        VideoEducativo video = new VideoEducativo();
        asignarCampos(video, datos, url);
        video.setActivo(true);
        return repositorio.save(video);
    }

    @Transactional
    public VideoEducativo actualizar(Long id, VideoEducativoRequestDTO datos) {
        VideoEducativo video = buscar(id);
        String url = validarUrl(datos.urlVideo());
        comprobarUrlDuplicada(url, id);

        asignarCampos(video, datos, url);
        // Editar no reactiva un video desactivado.
        return repositorio.save(video);
    }

    @Transactional
    public void desactivar(Long id) {
        VideoEducativo video = buscar(id);
        video.setActivo(false);
        repositorio.save(video);
    }

    @Transactional
    public VideoEducativo reactivar(Long id) {
        VideoEducativo video = buscar(id);
        video.setActivo(true);
        return repositorio.save(video);
    }

    private VideoEducativo buscar(Long id) {
        return repositorio.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Video educativo no encontrado"));
    }

    private void asignarCampos(VideoEducativo video,
                                VideoEducativoRequestDTO datos,
                                String urlValidada) {
        video.setTitulo(datos.titulo().trim());
        video.setDescripcion(datos.descripcion().trim());
        video.setUrlVideo(urlValidada);
        video.setCategoria(datos.categoria().trim());
        video.setNivel(datos.nivel().trim().toUpperCase(Locale.ROOT));
    }

    private String validarUrl(String urlIngresada) {
        if (urlIngresada == null || urlIngresada.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Se requiere la URL del video");
        }

        String url = urlIngresada.trim();
        try {
            URI uri = URI.create(url);
            String host = uri.getHost();
            if (!"https".equalsIgnoreCase(uri.getScheme())
                    || host == null
                    || !PLATAFORMAS_PERMITIDAS.contains(host.toLowerCase(Locale.ROOT))
                    || uri.getUserInfo() != null
                    || (uri.getPort() != -1 && uri.getPort() != 443)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Solo se admiten enlaces HTTPS de YouTube o Vimeo");
            }
            return url;
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La URL del video no es válida");
        }
    }

    private void comprobarUrlDuplicada(String url, Long idActual) {
        boolean duplicado = repositorio.findAll().stream().anyMatch(video ->
                video.getUrlVideo().equalsIgnoreCase(url)
                        && (idActual == null || !video.getIdVideo().equals(idActual)));
        if (duplicado) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe un video con esa URL");
        }
    }
}
