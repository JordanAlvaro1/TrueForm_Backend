
package com.trueform.backend.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "glosario_terminos")
public class GlosarioTermino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_termino")
    private Long idTermino;

    @Column(nullable = false, unique = true, length = 120)
    private String termino;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String definicion;

    @Column(nullable = false, length = 80)
    private String categoria;

    @Column(nullable = false)
    private Boolean activo = true;
}
