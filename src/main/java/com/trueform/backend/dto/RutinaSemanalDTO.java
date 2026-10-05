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
public class RutinaSemanalDTO {
    private Long idRutina;
    private Long idUsuario;
    private String nombreRutina;
    private String descripcion;
    private List<DiaRutinaDTO> dias;
}
