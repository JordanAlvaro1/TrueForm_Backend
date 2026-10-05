package com.trueform.backend.controladores;

import com.trueform.backend.dto.TipDTO;
import com.trueform.backend.servicios.TipServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tips")
public class TipController {

    @Autowired
    private TipServicio tipServicio;

    // HU6 - Visualizar  tips básicos de entrenamiento por día
    @GetMapping("/hoy")
    public ResponseEntity<List<TipDTO>> obtenerTipsDelDia() {

        List<TipDTO> tips =
                tipServicio.obtenerTipsDelDia();

        return ResponseEntity.ok(tips);
    }
}
