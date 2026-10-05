package com.trueform.backend.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@Table(name = "registros_entrenamiento")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RegistroEntrenamiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRegistro;
    @ManyToOne @JoinColumn(name = "id_rutina", nullable = false)
    private Rutina rutina;
    @ManyToOne @JoinColumn(name = "id_ejercicio", nullable = false)
    private Ejercicio ejercicio;
    @Column(nullable = false)
    private LocalDate fecha;
    @Column(nullable = false)
    private Integer series;
    @Column(nullable = false)
    private Integer repeticiones;
    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal pesoKg;
    @Column(nullable = false, length = 150)
    private String nombreRutina;
    @Column(nullable = false, length = 150)
    private String nombreEjercicio;
}
