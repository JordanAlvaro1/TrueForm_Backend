package com.trueform.backend.controladores;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class CsrfController {
    // Conservar la cookie JSESSIONID y enviar token en headerName al escribir datos.
    @GetMapping("/csrf")
    public CsrfToken csrf(CsrfToken token) {
        return token;
    }
}
