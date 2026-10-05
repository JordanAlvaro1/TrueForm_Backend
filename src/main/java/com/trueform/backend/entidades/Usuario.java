package com.trueform.backend.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "edad", nullable = false)
    private Integer edad;

    @Column(precision = 5, scale = 2)
    private BigDecimal peso;

    @Column(precision = 5, scale = 2)
    private BigDecimal altura;

    @Column(name = "nivel", nullable = false, length = 20)
    private String nivel;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @ManyToOne
    @JoinColumn(name = "id_objetivo", nullable = false)
    private Objetivo objetivo;

    @Column(name = "equipo", columnDefinition = "TEXT")
    private String equipo;

    // Los registros anteriores con rol null se consideran USER.
    @Column(length = 20)
    private String rol = "USER";

    @PrePersist
    public void prePersist() {
        fechaRegistro = LocalDateTime.now();
    }
}
