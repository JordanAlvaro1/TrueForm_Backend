package com.trueform.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AjusteRutinaDTO {

    private Long idUsuario;
    private Long idRutina;

    private Boolean datosSuficientes;
    private Boolean estancamientoDetectado;

    private String recomendacion;
    private String mensaje;
}
