package com.trueform.backend.dto;

import lombok.*;
import java.time.LocalDate;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DescansoDTO {
    private Long idRutinaEjercicio;
    private Integer descansoSegundos;
}
