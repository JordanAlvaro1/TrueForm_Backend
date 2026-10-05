package com.trueform.backend.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rutina_ejercicios")
public class RutinaEjercicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rutina_ejercicio")
    private Long idRutinaEjercicio;

    @Column(name = "series", nullable = false)
    private Integer series;

    @Column(name = "repeticiones", nullable = false)
    private Integer repeticiones;

    @Column(name = "descanso_segundos")
    private Integer descansoSegundos;

    @Column(name = "dia_semana", length = 20)
    private String diaSemana;

    @Column(name = "orden_ejercicio")
    private Integer ordenEjercicio;


    @ManyToOne
    @JoinColumn(name = "id_rutina", nullable = false)
    private Rutina rutina;

    @ManyToOne
    @JoinColumn(name = "id_ejercicio", nullable = false)
    private Ejercicio ejercicio;
}
