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
@Table(name = "ejercicios")
public class Ejercicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ejercicio")
    private Long idEjercicio;

    @Column(name = "nombre_ejercicio", nullable = false, length = 150)
    private String nombreEjercicio;

    @Column(name = "grupo_muscular", nullable = false, length = 100)
    private String grupoMuscular;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "tipo", length = 50)
    private String tipo;

    @Column(name = "equipo_necesario", length = 150)
    private String equipoNecesario;
}
