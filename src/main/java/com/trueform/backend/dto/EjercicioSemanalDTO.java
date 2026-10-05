package com.trueform.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EjercicioSemanalDTO {
    private Long idEjercicio;
    private String nombreEjercicio;
    private String grupoMuscular;
    private Integer series;
    private Integer repeticiones;
    private Integer descansoSegundos;
    private Integer ordenEjercicio;
}
