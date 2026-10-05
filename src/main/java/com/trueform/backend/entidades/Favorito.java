package com.trueform.backend.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@Table(name = "favoritos", uniqueConstraints = @UniqueConstraint(columnNames = {"id_usuario", "id_ejercicio"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Favorito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFavorito;
    @ManyToOne @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
    @ManyToOne @JoinColumn(name = "id_ejercicio", nullable = false)
    private Ejercicio ejercicio;
}
