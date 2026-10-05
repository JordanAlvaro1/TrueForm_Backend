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
public class ProgresoDTO {
    private Long idProgreso;
    private Long idUsuario;
    private Long idRutina;
    private LocalDateTime fechaRegistro;
    private BigDecimal pesoActual;
    private BigDecimal porcentajeCumplimiento;
    private String observaciones;
}
