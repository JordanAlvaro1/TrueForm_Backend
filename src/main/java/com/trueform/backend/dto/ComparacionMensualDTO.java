package com.trueform.backend.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ComparacionMensualDTO {
    private Long idUsuario;
    private boolean hayDatos;
    private String mensaje;
    // Dos elementos: mes base y mes a comparar. Sirven para las gráficas del frontend.
    private List<ResumenProgresoDTO> meses;
    private BigDecimal diferenciaPesoPromedioKg;
    private BigDecimal diferenciaCumplimientoPuntos;
}
