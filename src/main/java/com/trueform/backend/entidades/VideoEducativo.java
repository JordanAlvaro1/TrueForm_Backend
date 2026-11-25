
package com.trueform.backend.entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "videos_educativos")
public class VideoEducativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_video")
    private Long idVideo;

    @Column(nullable = false, length = 160)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "url_video", nullable = false, length = 1000)
    private String urlVideo;

    @Column(nullable = false, length = 80)
    private String categoria;

    @Column(nullable = false, length = 30)
    private String nivel;

    @Column(nullable = false)
    private Boolean activo = true;
}
