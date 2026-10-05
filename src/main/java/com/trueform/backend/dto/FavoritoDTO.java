package com.trueform.backend.dto;

import lombok.*;
import java.time.LocalDate;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class FavoritoDTO {
    private Long idFavorito;
    private Long idUsuario;
    private Long idEjercicio;
    private String nombreEjercicio;
}
