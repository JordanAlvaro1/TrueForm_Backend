package com.trueform.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RutinaEjercicioDTO {
    private Long idRutinaEjercicio;
    private Long idRutina;
    private Long idEjercicio;
    private Integer series;
    private Integer repeticiones;
    private Integer descansoSegundos;
    private String diaSemana;
    private Integer ordenEjercicio;
}
