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
public class ResumenProgresoDTO {
    private Long idUsuario;
    private String periodo;
    private boolean hayDatos;
    private String mensaje;
    private int registrosProgreso;
    private int registrosConPeso;
    private int registrosConCumplimiento;
    private LocalDateTime desde;
    private LocalDateTime hasta;
    private BigDecimal pesoPromedioKg;
    private BigDecimal pesoInicialKg;
    private BigDecimal pesoFinalKg;
    private BigDecimal cumplimientoPromedio;
}
