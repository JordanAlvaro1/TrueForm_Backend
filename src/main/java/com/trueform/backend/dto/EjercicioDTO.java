package com.trueform.backend.dto;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EjercicioDTO {
    private Long idEjercicio;
    private String nombreEjercicio;
    private String grupoMuscular;
    private String descripcion;
    private String tipo;
    private String equipoNecesario;
}
