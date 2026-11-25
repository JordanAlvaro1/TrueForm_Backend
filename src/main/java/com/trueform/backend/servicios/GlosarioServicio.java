
package com.trueform.backend.servicios;

import com.trueform.backend.dto.GlosarioDTO;
import com.trueform.backend.entidades.GlosarioTermino;
import com.trueform.backend.repositorios.GlosarioTerminoRepositorio;

import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Service
public class GlosarioServicio {

    private final GlosarioTerminoRepositorio repositorio;

    public GlosarioServicio(
            GlosarioTerminoRepositorio repositorio) {

        this.repositorio = repositorio;
    }

    // Sinónimos reconocidos por el glosario original.
    private static final Map<String, String> SINONIMOS = Map.ofEntries(
            Map.entry("series", "serie"),
            Map.entry("repeticion", "repetición"),
            Map.entry("repeticiones", "repetición"),
            Map.entry("progresion", "progresión"),
            Map.entry("peso", "carga"),
            Map.entry("fallo", "fallo muscular"),
            Map.entry("rom", "rango de movimiento"),
            Map.entry("cardiovascular", "cardio"),
            Map.entry("ejercicios compuestos", "ejercicio compuesto"),
            Map.entry("ejercicios aislados", "ejercicio aislado"),
            Map.entry("superseries", "superserie"),
            Map.entry("dropset", "drop set"),
            Map.entry("rm", "repetición máxima"),
            Map.entry("1rm", "repetición máxima"),
            Map.entry("repeticion maxima", "repetición máxima"),
            Map.entry("recuperacion", "recuperación")
    );

    public GlosarioDTO buscarTermino(String termino) {

        // Validar que se haya ingresado una palabra.
        if (termino == null || termino.isBlank()) {
            return new GlosarioDTO(
                    termino,
                    null,
                    false,
                    "Debes ingresar un término para realizar la búsqueda."
            );
        }

        String busqueda = termino.trim();

        // Primero buscar exactamente el término solicitado.
        Optional<GlosarioTermino> resultado =
                repositorio.findByTerminoIgnoreCaseAndActivoTrue(
                        busqueda
                );

        // Si no existe, revisar los sinónimos originales.
        if (resultado.isEmpty()) {

            String normalizado = Normalizer.normalize(
                    busqueda.toLowerCase(Locale.ROOT),
                    Normalizer.Form.NFD
            ).replaceAll("\\p{M}+", "");

            String terminoCanonico =
                    SINONIMOS.getOrDefault(normalizado, busqueda);

            resultado =
                    repositorio.findByTerminoIgnoreCaseAndActivoTrue(
                            terminoCanonico
                    );
        }

        // Devolver la definición encontrada en PostgreSQL.
        if (resultado.isPresent()) {

            GlosarioTermino registro = resultado.get();

            return new GlosarioDTO(
                    termino,
                    registro.getDefinicion(),
                    true,
                    "Término encontrado."
            );
        }

        return new GlosarioDTO(
                termino,
                null,
                false,
                "Término no encontrado. Puedes consultar al asistente virtual de TrueForm."
        );
    }
}
