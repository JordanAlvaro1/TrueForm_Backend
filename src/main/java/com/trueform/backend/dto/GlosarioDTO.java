package com.trueform.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GlosarioDTO {

    private String termino;
    private String definicion;
    private Boolean encontrado;
    private String mensaje;
}
