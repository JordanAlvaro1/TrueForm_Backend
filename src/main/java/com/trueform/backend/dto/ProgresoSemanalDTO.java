package com.trueform.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProgresoSemanalDTO {
    private Long idUsuario;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    private Integer sesionesRegistradas;
    private BigDecimal promedioCumplimiento;

    private List<ProgresoDiaDTO> datos;

    private String mensaje;
}
