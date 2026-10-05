package com.trueform.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EjercicioGuiadoDTO {

    private Long idRutinaEjercicio;
    private Long idEjercicio;
    private String nombreEjercicio;
    private String descripcion;
    private String grupoMuscular;

    private Integer series;
    private Integer repeticiones;
    private Integer descansoSegundos;

    private String diaSemana;
    private Integer ordenEjercicio;

    private Boolean ultimoEjercicio;
    private String mensaje;
}
