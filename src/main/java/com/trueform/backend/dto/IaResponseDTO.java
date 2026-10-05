package com.trueform.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IaResponseDTO {

    private Long idUsuario;
    private String pregunta;
    private String respuesta;
    private Boolean respondido;
    private String mensaje;
}
