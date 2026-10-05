package com.trueform.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OnboardingDTO {
    @NotNull(message = "El usuario es obligatorio")
    private Long idUsuario;

    @NotBlank(message = "Selecciona tu nivel")
    private String nivel;

    @NotNull(message = "Selecciona tu objetivo")
    private Long idObjetivo;

    @NotEmpty(message = "Selecciona tu equipo disponible")
    private List<String> equipo;
}
