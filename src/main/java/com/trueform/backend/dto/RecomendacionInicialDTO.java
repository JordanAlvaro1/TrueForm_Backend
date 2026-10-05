package com.trueform.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecomendacionInicialDTO {
    private Long idUsuario;
    private String nivel;
    private String objetivo;
    private String recomendacion;
    private String mensaje;
}
