package com.trueform.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record VideoEducativoRequestDTO(
        @NotBlank(message = "El título es obligatorio")
        @Size(max = 160, message = "El título supera 160 caracteres")
        String titulo,

        @NotBlank(message = "La descripción es obligatoria")
        @Size(max = 4000, message = "La descripción supera 4000 caracteres")
        String descripcion,

        @NotBlank(message = "La URL del video es obligatoria")
        @Size(max = 1000, message = "La URL supera 1000 caracteres")
        String urlVideo,

        @NotBlank(message = "La categoría es obligatoria")
        @Size(max = 80, message = "La categoría supera 80 caracteres")
        String categoria,

        @NotBlank(message = "El nivel es obligatorio")
        @Pattern(
                regexp = "(?i)^(GENERAL|PRINCIPIANTE|INTERMEDIO|AVANZADO)$",
                message = "Nivel permitido: GENERAL, PRINCIPIANTE, INTERMEDIO o AVANZADO"
        )
        String nivel
) {}
