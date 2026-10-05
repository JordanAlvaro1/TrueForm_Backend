package com.trueform.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private Long idUsuario;
    private String nombres;
    private String apellidos;
    private String email;
    private Integer edad;
    private BigDecimal peso;
    private BigDecimal altura;
    private String nivel;
    private LocalDateTime fechaRegistro;
    private Long idObjetivo;
    private String equipo;
}
