package com.trueform.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecomendacionProgresoDTO {

    private Long idUsuario;

    private Boolean datosSuficientes;

    private String tipoRecomendacion;

    private String recomendacion;

    private String mensaje;
}
