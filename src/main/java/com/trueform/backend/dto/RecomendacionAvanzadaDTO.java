package com.trueform.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecomendacionAvanzadaDTO {

    private Long idUsuario;

    private Boolean datosSuficientes;

    private String intensidad;

    private String volumen;

    private String recuperacion;

    private String variacionEjercicios;

    private String mensaje;
}
