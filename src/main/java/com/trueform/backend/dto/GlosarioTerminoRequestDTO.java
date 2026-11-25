package com.trueform.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GlosarioTerminoRequestDTO(
        @NotBlank(message = "El termino es obligatorio")
        @Size(max = 120, message = "El termino no puede superar 120 caracteres")
        String termino,

        @NotBlank(message = "La definicion es obligatoria")
        @Size(max = 4000, message = "La definicion es demasiado extensa")
        String definicion,

        @NotBlank(message = "La categoria es obligatoria")
        @Size(max = 80, message = "La categoria no puede superar 80 caracteres")
        String categoria
) {}
