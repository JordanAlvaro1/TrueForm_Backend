package com.trueform.backend.dto;

import lombok.*;
import java.time.LocalDate;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroEntrenamientoDTO {
    private Long idRegistro;
    private Long idRutina;
    private Long idEjercicio;
    private Long idUsuario;
    private String nombreRutina;
    private String nombreEjercicio;
    private LocalDate fecha;
    private Integer series;
    private Integer repeticiones;
    private BigDecimal pesoKg;
}
