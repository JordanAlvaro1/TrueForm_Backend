package com.trueform.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TipContenidoRequestDTO(
        @NotBlank(message = "La categoría es obligatoria")
        @Size(max = 80, message = "La categoría no puede superar 80 caracteres")
        String categoria,

        @NotBlank(message = "El título es obligatorio")
        @Size(max = 160, message = "El título no puede superar 160 caracteres")
        String titulo,

        @NotBlank(message = "El contenido es obligatorio")
        @Size(max = 4000, message = "El contenido no puede superar 4000 caracteres")
        String contenido,

        @NotNull(message = "El orden de visualización es obligatorio")
        @Min(value = 1, message = "El orden debe ser mayor o igual a 1")
        Integer ordenVisualizacion
) {}
