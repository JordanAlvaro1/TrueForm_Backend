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
@Table(name = "objetivos")
public class Objetivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_objetivo")
    private Long idObjetivo;

    @Column(name = "nombre_objetivo", nullable = false, length = 100)
    private String nombreObjetivo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;
}
