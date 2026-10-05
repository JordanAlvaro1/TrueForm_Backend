package com.trueform.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class SeleccionObjetivoDTO {
    @NotNull(message = "Debes seleccionar un objetivo")
    private Long idObjetivo;
}
