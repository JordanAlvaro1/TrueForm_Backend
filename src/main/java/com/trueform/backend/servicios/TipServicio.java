
package com.trueform.backend.servicios;

import com.trueform.backend.dto.TipDTO;
import com.trueform.backend.entidades.TipContenido;
import com.trueform.backend.repositorios.TipContenidoRepositorio;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TipServicio {

    private final TipContenidoRepositorio repositorio;

    public TipServicio(TipContenidoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public List<TipDTO> obtenerTipsDelDia() {

        // 1. Consultar los consejos activos desde PostgreSQL.
        List<TipContenido> tips =
                repositorio
                        .findByActivoTrueOrderByOrdenVisualizacionAscIdTipAsc();

        // 2. Si no existen consejos activos, devolver lista vacía.
        if (tips.isEmpty()) {
            return new ArrayList<>();
        }

        // 3. Mantener la rotación diaria del servicio original.
        int dia = LocalDate.now().getDayOfYear();

        int inicio = (dia * 4) % tips.size();

        // 4. Mostrar como máximo cuatro consejos.
        int cantidad = Math.min(4, tips.size());

        List<TipDTO> tipsDelDia = new ArrayList<>();

        for (int i = 0; i < cantidad; i++) {

            int posicion = (inicio + i) % tips.size();

            TipContenido tip = tips.get(posicion);

            // 5. Convertir la entidad a TipDTO.
            tipsDelDia.add(
                    new TipDTO(
                            tip.getCategoria(),
                            tip.getTitulo(),
                            tip.getContenido()
                    )
            );
        }

        return tipsDelDia;
    }
}
