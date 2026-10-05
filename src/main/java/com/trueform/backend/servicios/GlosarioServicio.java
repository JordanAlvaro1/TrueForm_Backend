package com.trueform.backend.servicios;

import com.trueform.backend.dto.GlosarioDTO;
import org.springframework.stereotype.Service;

@Service
public class GlosarioServicio {

    public GlosarioDTO buscarTermino(String termino) {

        // Validamos que el usuario haya escrito algo
        if (termino == null || termino.isBlank()) {
            return new GlosarioDTO(
                    termino,
                    null,
                    false,
                    "Debes ingresar un término para realizar la búsqueda."
            );
        }

        String terminoBuscado = termino.trim().toLowerCase();

        switch (terminoBuscado) {

            case "serie":
            case "series":
                return new GlosarioDTO(
                        termino,
                        "Conjunto de repeticiones realizadas de un ejercicio.",
                        true,
                        "Término encontrado."
                );

            case "repeticion":
            case "repetición":
            case "repeticiones":
                return new GlosarioDTO(
                        termino,
                        "Número de veces que se realiza un movimiento o ejercicio dentro de una serie.",
                        true,
                        "Término encontrado."
                );

            case "descanso":
                return new GlosarioDTO(
                        termino,
                        "Tiempo de recuperación entre series, ejercicios o sesiones de entrenamiento.",
                        true,
                        "Término encontrado."
                );

            case "calentamiento":
                return new GlosarioDTO(
                        termino,
                        "Actividad realizada antes del entrenamiento para preparar músculos y articulaciones.",
                        true,
                        "Término encontrado."
                );

            case "progresion":
            case "progresión":
                return new GlosarioDTO(
                        termino,
                        "Aumento gradual de la dificultad del entrenamiento mediante peso, repeticiones, series u otras variables.",
                        true,
                        "Término encontrado."
                );

            case "rutina":
                return new GlosarioDTO(
                        termino,
                        "Conjunto organizado de ejercicios planificados para alcanzar un objetivo de entrenamiento.",
                        true,
                        "Término encontrado."
                );

            case "volumen":
                return new GlosarioDTO(
                        termino,
                        "Cantidad total de trabajo realizado durante un entrenamiento.",
                        true,
                        "Término encontrado."
                );

            case "intensidad":
                return new GlosarioDTO(
                        termino,
                        "Nivel de esfuerzo o dificultad aplicado durante un ejercicio o entrenamiento.",
                        true,
                        "Término encontrado."
                );

            case "carga":
            case "peso":
                return new GlosarioDTO(
                        termino,
                        "Resistencia utilizada durante un ejercicio, como el peso de una mancuerna, barra o máquina.",
                        true,
                        "Término encontrado."
                );

            case "fallo muscular":
            case "fallo":
                return new GlosarioDTO(
                        termino,
                        "Punto en el que no es posible completar otra repetición manteniendo una ejecución adecuada.",
                        true,
                        "Término encontrado."
                );

            case "rango de movimiento":
            case "rom":
                return new GlosarioDTO(
                        termino,
                        "Recorrido que realiza una articulación durante la ejecución de un ejercicio.",
                        true,
                        "Término encontrado."
                );

            case "movilidad":
                return new GlosarioDTO(
                        termino,
                        "Capacidad de mover una articulación de forma controlada dentro de un rango de movimiento.",
                        true,
                        "Término encontrado."
                );

            case "cardio":
            case "cardiovascular":
                return new GlosarioDTO(
                        termino,
                        "Actividad física que aumenta la frecuencia cardíaca y ayuda a mejorar la resistencia cardiovascular.",
                        true,
                        "Término encontrado."
                );

            case "fuerza":
                return new GlosarioDTO(
                        termino,
                        "Capacidad de los músculos para generar tensión y vencer o soportar una resistencia.",
                        true,
                        "Término encontrado."
                );

            case "resistencia":
                return new GlosarioDTO(
                        termino,
                        "Capacidad del cuerpo o de los músculos para mantener un esfuerzo durante un periodo determinado.",
                        true,
                        "Término encontrado."
                );

            case "hipertrofia":
                return new GlosarioDTO(
                        termino,
                        "Proceso de aumento del tamaño muscular como resultado del entrenamiento y la recuperación.",
                        true,
                        "Término encontrado."
                );

            case "frecuencia":
                return new GlosarioDTO(
                        termino,
                        "Cantidad de veces que se entrena un músculo o se realiza una actividad durante un periodo determinado.",
                        true,
                        "Término encontrado."
                );

            case "ejercicio compuesto":
            case "ejercicios compuestos":
                return new GlosarioDTO(
                        termino,
                        "Ejercicio que involucra varias articulaciones y grupos musculares al mismo tiempo.",
                        true,
                        "Término encontrado."
                );

            case "ejercicio aislado":
            case "ejercicios aislados":
                return new GlosarioDTO(
                        termino,
                        "Ejercicio enfocado principalmente en una articulación o grupo muscular específico.",
                        true,
                        "Término encontrado."
                );

            case "superserie":
            case "superseries":
                return new GlosarioDTO(
                        termino,
                        "Método que consiste en realizar dos ejercicios consecutivos con poco o ningún descanso entre ellos.",
                        true,
                        "Término encontrado."
                );

            case "drop set":
            case "dropset":
                return new GlosarioDTO(
                        termino,
                        "Técnica en la que se reduce la carga después de alcanzar un nivel alto de fatiga para continuar realizando repeticiones.",
                        true,
                        "Término encontrado."
                );

            case "rm":
            case "1rm":
            case "repeticion maxima":
            case "repetición máxima":
                return new GlosarioDTO(
                        termino,
                        "Cantidad máxima de peso que una persona puede levantar una vez en un ejercicio determinado.",
                        true,
                        "Término encontrado."
                );

            case "rpe":
                return new GlosarioDTO(
                        termino,
                        "Escala utilizada para estimar qué tan intenso o difícil se percibe un esfuerzo durante el entrenamiento.",
                        true,
                        "Término encontrado."
                );

            case "recuperacion":
            case "recuperación":
                return new GlosarioDTO(
                        termino,
                        "Proceso mediante el cual el cuerpo descansa y se adapta después del entrenamiento.",
                        true,
                        "Término encontrado."
                );

            case "enfriamiento":
                return new GlosarioDTO(
                        termino,
                        "Periodo de actividad ligera realizado al finalizar el entrenamiento para reducir progresivamente la intensidad.",
                        true,
                        "Término encontrado."
                );

            default:
                return new GlosarioDTO(
                        termino,
                        null,
                        false,
                        "Término no encontrado. Puedes consultar al asistente virtual de TrueForm."
                );
        }
    }
}
