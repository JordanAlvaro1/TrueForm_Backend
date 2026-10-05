package com.trueform.backend.dto;

import lombok.*;
import java.time.LocalDate;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class EntrenamientoProgramadoDTO {
    private Long idProgramacion;
    private Long idRutina;
    private Long idUsuario;
    private String nombreRutina;
    private LocalDate fecha;
    private String estado;
}
