package com.trueform.backend.controladores;

import com.trueform.backend.entidades.VideoEducativo;
import com.trueform.backend.servicios.VideoEducativoServicio;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
public class VideoEducativoController {

    private final VideoEducativoServicio servicio;

    public VideoEducativoController(VideoEducativoServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<VideoEducativo> listarPublicados(
            @RequestParam(required = false) String nivel) {
        return servicio.listarPublicados(nivel);
    }
}
