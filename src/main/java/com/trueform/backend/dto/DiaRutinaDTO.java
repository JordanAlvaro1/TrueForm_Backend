package com.trueform.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiaRutinaDTO {
    private String dia;
    private String grupoMuscular;
    private Boolean descanso;
    private String mensaje;
    private List<EjercicioSemanalDTO> ejercicios;
}
