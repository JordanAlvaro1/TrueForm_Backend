package com.trueform.backend.dto;

import lombok.*;
import java.time.LocalDate;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResumenSemanalDTO {
    private Long idUsuario;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer programados;
    private Integer completados;
    private Integer pendientes;
    private Integer cancelados;
    private Double porcentajeCumplimiento;
}
