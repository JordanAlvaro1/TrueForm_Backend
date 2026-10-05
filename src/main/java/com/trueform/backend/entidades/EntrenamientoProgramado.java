package com.trueform.backend.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@Table(name = "entrenamientos_programados")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class EntrenamientoProgramado {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProgramacion;
    @ManyToOne @JoinColumn(name = "id_rutina", nullable = false)
    private Rutina rutina;
    @Column(nullable = false)
    private LocalDate fecha;
    @Column(nullable = false, length = 20)
    private String estado;
}
