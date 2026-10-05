package com.trueform.backend.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculosProgreso {
    private CalculosProgreso() { }

    public static BigDecimal promedio(BigDecimal suma, int cantidad) {
        if (cantidad == 0) return null;
        return suma.divide(BigDecimal.valueOf(cantidad), 2, RoundingMode.HALF_UP);
    }

    public static BigDecimal diferencia(BigDecimal base, BigDecimal comparado) {
        if (base == null || comparado == null) return null;
        return comparado.subtract(base);
    }

    public static String celda(Object valor) {
        // El CSV de resumen solo recibe números y fechas generados por el API.
        return valor == null ? "" : valor.toString();
    }
}
