package com.trueform.backend.controladores;

import com.trueform.backend.dto.GlosarioDTO;
import com.trueform.backend.servicios.GlosarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/glosario")
public class GlosarioController {

    @Autowired
    private GlosarioServicio glosarioServicio;

    // HU7
    @GetMapping("/buscar")
    public ResponseEntity<GlosarioDTO> buscarTermino(
            @RequestParam String termino) {

        GlosarioDTO resultado =
                glosarioServicio.buscarTermino(termino);

        return ResponseEntity.ok(resultado);
    }
}
