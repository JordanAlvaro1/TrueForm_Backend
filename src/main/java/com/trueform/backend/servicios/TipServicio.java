package com.trueform.backend.servicios;

import com.trueform.backend.dto.TipDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TipServicio {

    public List<TipDTO> obtenerTipsDelDia() {

        List<TipDTO> tips = new ArrayList<>();

        tips.add(new TipDTO(
                "Técnica",
                "Prioriza una buena ejecución",
                "Realiza cada ejercicio de forma controlada antes de aumentar el peso."
        ));

        tips.add(new TipDTO(
                "Descanso",
                "Respeta tus tiempos de recuperación",
                "Dormir y descansar adecuadamente ayuda a mejorar tu rendimiento y recuperación muscular."
        ));

        tips.add(new TipDTO(
                "Constancia",
                "Mantén una rutina constante",
                "Los resultados requieren tiempo. Entrena de manera constante y registra tu progreso."
        ));

        tips.add(new TipDTO(
                "Hidratación",
                "Mantente hidratado",
                "Consume suficiente agua antes, durante y después del entrenamiento."
        ));

        tips.add(new TipDTO(
                "Calentamiento",
                "Prepara tu cuerpo",
                "Realiza un calentamiento antes de comenzar para preparar músculos y articulaciones."
        ));

        tips.add(new TipDTO(
                "Progresión",
                "Aumenta gradualmente",
                "Incrementa el peso o las repeticiones de manera progresiva según tu capacidad."
        ));

        tips.add(new TipDTO(
                "Respiración",
                "Controla tu respiración",
                "Mantén un patrón de respiración controlado durante cada ejercicio."
        ));

        tips.add(new TipDTO(
                "Movilidad",
                "Cuida tu rango de movimiento",
                "Trabaja la movilidad para realizar los ejercicios con un rango de movimiento adecuado."
        ));

        tips.add(new TipDTO(
                "Recuperación",
                "Escucha a tu cuerpo",
                "Si sientes fatiga excesiva, reduce la intensidad y prioriza la recuperación."
        ));

        tips.add(new TipDTO(
                "Técnica",
                "No sacrifiques técnica por peso",
                "Utiliza una carga que te permita mantener una ejecución correcta durante todas las repeticiones."
        ));

        tips.add(new TipDTO(
                "Descanso",
                "Duerme lo suficiente",
                "Un descanso adecuado contribuye a la recuperación y al rendimiento durante tus entrenamientos."
        ));

        tips.add(new TipDTO(
                "Constancia",
                "Registra tu progreso",
                "Llevar un registro de tus entrenamientos te permite observar tus avances con el tiempo."
        ));


        int dia = LocalDate.now().getDayOfYear();

        // Posición inicial diferente cada día
        int inicio = (dia * 4) % tips.size();

        List<TipDTO> tipsDelDia = new ArrayList<>();


        for (int i = 0; i < 4; i++) {

            int posicion = (inicio + i) % tips.size();

            tipsDelDia.add(tips.get(posicion));
        }

        return tipsDelDia;
    }
}