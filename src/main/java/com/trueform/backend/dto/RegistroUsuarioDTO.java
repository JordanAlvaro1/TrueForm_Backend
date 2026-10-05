package com.trueform.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistroUsuarioDTO {
    private String nombres;
    private String apellidos;
    @jakarta.validation.constraints.Email
    private String email;
    private String password;
    private Integer edad;
    private BigDecimal peso;
    private BigDecimal altura;
    private String nivel;
    private Long idObjetivo;
    private String equipo;
}
