package com.trueform.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistorialProgresoDetalleDTO {

    private Long idProgreso;
    private LocalDateTime fecha;
    private String rutina;
    private String ejercicios;
    private BigDecimal porcentajeCumplimiento;
    private BigDecimal pesoActual;
    private String observaciones;
}
