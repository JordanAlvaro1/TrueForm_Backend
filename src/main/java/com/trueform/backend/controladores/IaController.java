package com.trueform.backend.controladores;

import com.trueform.backend.dto.IaRequestDTO;
import com.trueform.backend.dto.IaResponseDTO;
import com.trueform.backend.servicios.IaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ia")
public class IaController {

    @Autowired
    private IaServicio iaServicio;

    // HU13
    @PostMapping("/chat")
    public ResponseEntity<IaResponseDTO> responderPregunta(
            @RequestBody IaRequestDTO request) {

        IaResponseDTO respuesta =
                iaServicio.responderPregunta(request);

        if (respuesta == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(respuesta);
    }
}
