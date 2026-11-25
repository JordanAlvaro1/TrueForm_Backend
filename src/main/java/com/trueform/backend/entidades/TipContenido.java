package com.trueform.backend.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tips_contenido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipContenido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tip")
    private Long idTip;

    @Column(nullable = false, length = 80)
    private String categoria;

    @Column(nullable = false, unique = true, length = 160)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenido;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "orden_visualizacion", nullable = false)
    private Integer ordenVisualizacion;
}
