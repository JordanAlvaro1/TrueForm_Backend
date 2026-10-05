package com.trueform.backend.servicios;

import com.trueform.backend.dto.*;
import com.trueform.backend.entidades.Progreso;
import com.trueform.backend.entidades.RutinaEjercicio;
import com.trueform.backend.repositorios.ProgresoRepositorio;
import com.trueform.backend.repositorios.RutinaEjercicioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.trueform.backend.entidades.Usuario;
import com.trueform.backend.entidades.Rutina;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class ProgresoServicio {
    @Autowired
    private AccesoServicio acceso;
    @Autowired
    private
    ProgresoRepositorio progresoRepositorio;

    @Autowired
    private
    UsuarioServicio usuarioServicio;

    @Autowired
    private
    RutinaServicio rutinaServicio;

    @Autowired
    private
    RutinaEjercicioRepositorio rutinaEjercicioRepositorio;


    public List<Progreso> listar() {
        return progresoRepositorio.findByUsuario_IdUsuario(acceso.idActual());
    }

    public Progreso buscarPorId(Long id) {
        Progreso encontrado = progresoRepositorio.findById(id).orElse(null);
        if (encontrado != null) acceso.verificarUsuario(encontrado.getUsuario().getIdUsuario());
        return encontrado;
    }

    public Progreso registrar(Progreso progreso) {
        acceso.verificarUsuario(progreso.getUsuario().getIdUsuario());
        acceso.verificarRutina(progreso.getRutina().getIdRutina());
        return progresoRepositorio.save(progreso);
    }

    public Progreso modificar(Progreso progreso) {
        acceso.verificarUsuario(progreso.getUsuario().getIdUsuario());
        acceso.verificarRutina(progreso.getRutina().getIdRutina());
        buscarPorId(progreso.getIdProgreso());
        return progresoRepositorio.save(progreso);
    }

    public void eliminar(Long id) {
        buscarPorId(id);
        progresoRepositorio.deleteById(id);
    }

    public List<Progreso> buscarPorUsuario(Long idUsuario) {
        acceso.verificarUsuario(idUsuario);
        return progresoRepositorio.findByUsuario_IdUsuario(idUsuario);
    }
    // HU9 - Finalizar rutina y registrar progreso
    public FinalizarRutinaDTO finalizarRutina(
            Long idUsuario,
            Long idRutina,
            BigDecimal porcentajeCumplimiento) {
        acceso.verificarUsuario(idUsuario);
        acceso.verificarRutina(idRutina);

        Usuario usuario = usuarioServicio.buscarPorId(idUsuario);
        Rutina rutina = rutinaServicio.buscarPorId(idRutina);

        if (usuario == null || rutina == null) {
            return null;
        }

        if (porcentajeCumplimiento == null ||
                porcentajeCumplimiento.compareTo(BigDecimal.ZERO) < 0 ||
                porcentajeCumplimiento.compareTo(new BigDecimal("100")) > 0) {
            return null;
        }

        boolean completada =
                porcentajeCumplimiento.compareTo(new BigDecimal("100")) == 0;

        String mensaje;
        String observacion;

        if (completada) {
            mensaje = "¡Excelente trabajo! Completaste tu rutina.";
            observacion = "Rutina completada.";
        } else {
            mensaje = "Buen trabajo. Tu progreso parcial ha sido registrado. Sigue avanzando.";
            observacion = "Rutina completada parcialmente.";
        }

        Progreso progreso = new Progreso();

        progreso.setUsuario(usuario);
        progreso.setRutina(rutina);
        progreso.setPorcentajeCumplimiento(porcentajeCumplimiento);
        progreso.setObservaciones(observacion);

        Progreso registrado = progresoRepositorio.save(progreso);

        return new FinalizarRutinaDTO(
                registrado.getIdProgreso(),
                usuario.getIdUsuario(),
                rutina.getIdRutina(),
                registrado.getFechaRegistro(),
                registrado.getPorcentajeCumplimiento(),
                completada,
                mensaje
        );
    }
    // HU10 - Obtener progreso de la semana actual
    public ProgresoSemanalDTO obtenerProgresoSemanal(Long idUsuario) {
        acceso.verificarUsuario(idUsuario);

        LocalDate hoy = LocalDate.now();

        // Obtener lunes y domingo de la semana actual
        LocalDate inicioSemana =
                hoy.with(DayOfWeek.MONDAY);

        LocalDate finSemana =
                hoy.with(DayOfWeek.SUNDAY);

        LocalDateTime fechaInicio =
                inicioSemana.atStartOfDay();

        LocalDateTime fechaFin =
                finSemana.atTime(LocalTime.MAX);

        // Buscar registros de progreso de esta semana
        List<Progreso> progresos =
                progresoRepositorio
                        .findByUsuario_IdUsuarioAndFechaRegistroBetweenOrderByFechaRegistroAsc(
                                idUsuario,
                                fechaInicio,
                                fechaFin
                        );

        List<ProgresoDiaDTO> datos = new ArrayList<>();

        // Caso: todavía no existen entrenamientos esta semana
        if (progresos.isEmpty()) {

            return new ProgresoSemanalDTO(
                    idUsuario,
                    inicioSemana,
                    finSemana,
                    0,
                    BigDecimal.ZERO,
                    datos,
                    "Aún no tienes sesiones registradas esta semana."
            );
        }

        BigDecimal suma = BigDecimal.ZERO;
        int registrosConPorcentaje = 0;

        for (Progreso progreso : progresos) {

            BigDecimal porcentaje =
                    progreso.getPorcentajeCumplimiento();

            if (porcentaje == null) {
                porcentaje = BigDecimal.ZERO;
            } else {
                suma = suma.add(porcentaje);
                registrosConPorcentaje++;
            }

            String dia =
                    progreso.getFechaRegistro()
                            .getDayOfWeek()
                            .getDisplayName(
                                    TextStyle.FULL,
                                    new Locale("es", "ES")
                            )
                            .toUpperCase();

            datos.add(
                    new ProgresoDiaDTO(
                            progreso.getFechaRegistro().toLocalDate(),
                            dia,
                            porcentaje
                    )
            );
        }

        BigDecimal promedio = BigDecimal.ZERO;

        if (registrosConPorcentaje > 0) {
            promedio = suma.divide(
                    BigDecimal.valueOf(registrosConPorcentaje),
                    2,
                    RoundingMode.HALF_UP
            );
        }

        return new ProgresoSemanalDTO(
                idUsuario,
                inicioSemana,
                finSemana,
                progresos.size(),
                promedio,
                datos,
                "Progreso semanal obtenido correctamente."
        );
    }

    // HU12 - Generar recomendación automática según el progreso
    public RecomendacionProgresoDTO obtenerRecomendacionProgreso(Long idUsuario) {
        acceso.verificarUsuario(idUsuario);

        Usuario usuario = usuarioServicio.buscarPorId(idUsuario);

        if (usuario == null) {
            return null;
        }

        List<Progreso> progresos =
                progresoRepositorio.findByUsuario_IdUsuario(idUsuario);

        // Si no existen registros de progreso
        if (progresos.isEmpty()) {

            return new RecomendacionProgresoDTO(
                    idUsuario,
                    false,
                    null,
                    null,
                    "Aún no hay suficientes datos de progreso para generar recomendaciones."
            );
        }

        BigDecimal suma = BigDecimal.ZERO;
        int cantidad = 0;

        for (Progreso progreso : progresos) {

            if (progreso.getPorcentajeCumplimiento() != null) {
                suma = suma.add(
                        progreso.getPorcentajeCumplimiento()
                );
                cantidad++;
            }
        }

        if (cantidad == 0) {

            return new RecomendacionProgresoDTO(
                    idUsuario,
                    false,
                    null,
                    null,
                    "Aún no hay suficientes datos de progreso para generar recomendaciones."
            );
        }

        BigDecimal promedio = suma.divide(
                BigDecimal.valueOf(cantidad),
                2,
                RoundingMode.HALF_UP
        );

        String tipoRecomendacion;
        String recomendacion;

        if (promedio.compareTo(new BigDecimal("50")) < 0) {

            tipoRecomendacion = "RECUPERACION";

            recomendacion =
                    "Reduce temporalmente la intensidad, prioriza la recuperación y aumenta la constancia en tus entrenamientos.";

        } else if (promedio.compareTo(new BigDecimal("80")) < 0) {

            tipoRecomendacion = "AJUSTE_ENTRENAMIENTO";

            recomendacion =
                    "Aumenta progresivamente la intensidad y mantén una recuperación adecuada.";

        } else {

            tipoRecomendacion = "PROGRESION";

            recomendacion =
                    "Tu cumplimiento es alto. Continúa progresando gradualmente con tu rutina actual.";
        }

        return new RecomendacionProgresoDTO(
                idUsuario,
                true,
                tipoRecomendacion,
                recomendacion,
                "Recomendación generada según tu progreso."
        );
    }
    // HU14 - Generar recomendaciones avanzadas de optimización
    public RecomendacionAvanzadaDTO obtenerRecomendacionAvanzada(Long idUsuario) {
        acceso.verificarUsuario(idUsuario);

        Usuario usuario = usuarioServicio.buscarPorId(idUsuario);

        if (usuario == null) {
            return null;
        }

        List<Progreso> progresos =
                progresoRepositorio.findByUsuario_IdUsuario(idUsuario);

        // Se requieren al menos 2 registros para analizar el progreso
        if (progresos.size() < 2) {

            return new RecomendacionAvanzadaDTO(
                    idUsuario,
                    false,
                    null,
                    null,
                    null,
                    null,
                    "Se necesitan más datos de entrenamiento para generar sugerencias personalizadas."
            );
        }

        BigDecimal suma = BigDecimal.ZERO;
        int cantidad = 0;

        for (Progreso progreso : progresos) {

            if (progreso.getPorcentajeCumplimiento() != null) {

                suma = suma.add(
                        progreso.getPorcentajeCumplimiento()
                );

                cantidad++;
            }
        }

        if (cantidad < 2) {

            return new RecomendacionAvanzadaDTO(
                    idUsuario,
                    false,
                    null,
                    null,
                    null,
                    null,
                    "Se necesitan más datos de entrenamiento para generar sugerencias personalizadas."
            );
        }

        BigDecimal promedio = suma.divide(
                BigDecimal.valueOf(cantidad),
                2,
                RoundingMode.HALF_UP
        );

        String intensidad;
        String volumen;
        String recuperacion;
        String variacionEjercicios;

        if (promedio.compareTo(new BigDecimal("50")) < 0) {

            intensidad =
                    "Reduce temporalmente la intensidad para mejorar la adherencia y la técnica.";

            volumen =
                    "Mantén un volumen moderado y prioriza completar correctamente las sesiones.";

            recuperacion =
                    "Prioriza el descanso y deja suficiente tiempo de recuperación entre entrenamientos.";

            variacionEjercicios =
                    "Mantén ejercicios conocidos antes de introducir variaciones más exigentes.";

        } else if (promedio.compareTo(new BigDecimal("80")) < 0) {

            intensidad =
                    "Aumenta progresivamente la intensidad de tus ejercicios.";

            volumen =
                    "Incrementa gradualmente el volumen mediante series o repeticiones adicionales.";

            recuperacion =
                    "Mantén períodos adecuados de recuperación para evitar fatiga acumulada.";

            variacionEjercicios =
                    "Introduce variaciones de ejercicios de forma progresiva para generar nuevos estímulos.";

        } else {

            intensidad =
                    "Puedes trabajar con una intensidad más alta manteniendo una progresión controlada.";

            volumen =
                    "Aumenta el volumen de entrenamiento gradualmente según tu capacidad de recuperación.";

            recuperacion =
                    "Mantén una recuperación adecuada y controla signos de fatiga antes de aumentar la carga.";

            variacionEjercicios =
                    "Incorpora nuevas variantes de ejercicios para continuar progresando y evitar estancamientos.";
        }

        return new RecomendacionAvanzadaDTO(
                idUsuario,
                true,
                intensidad,
                volumen,
                recuperacion,
                variacionEjercicios,
                "Recomendaciones avanzadas generadas según tu progreso."
        );
    }


    // HU16 - Obtener historial detallado de progreso físico
    public HistorialProgresoDTO obtenerHistorialProgreso(Long idUsuario) {
        acceso.verificarUsuario(idUsuario);

        Usuario usuario = usuarioServicio.buscarPorId(idUsuario);

        if (usuario == null) {
            return null;
        }

        List<Progreso> progresos =
                progresoRepositorio.findByUsuario_IdUsuario(idUsuario);

        // Caso: usuario sin registros
        if (progresos.isEmpty()) {

            return new HistorialProgresoDTO(
                    idUsuario,
                    false,
                    0,
                    new ArrayList<>(),
                    "Todavía no existen datos de progreso registrados."
            );
        }

        // Ordenar desde el registro más reciente al más antiguo
        progresos.sort(
                (p1, p2) ->
                        p2.getFechaRegistro()
                                .compareTo(p1.getFechaRegistro())
        );

        List<HistorialProgresoDetalleDTO> historial =
                new ArrayList<>();

        for (Progreso progreso : progresos) {

            Rutina rutina = progreso.getRutina();

            String nombreRutina =
                    rutina != null
                            ? rutina.getNombreRutina()
                            : "Sin rutina";

            String ejercicios = "Sin ejercicios registrados";

            if (rutina != null) {

                List<RutinaEjercicio> ejerciciosRutina =
                        rutinaEjercicioRepositorio
                                .findByRutina_IdRutinaOrderByOrdenEjercicioAsc(
                                        rutina.getIdRutina()
                                );

                if (!ejerciciosRutina.isEmpty()) {

                    ejercicios = ejerciciosRutina.stream()
                            .map(re -> re.getEjercicio().getNombreEjercicio())
                            .distinct()
                            .reduce((e1, e2) -> e1 + ", " + e2)
                            .orElse("Sin ejercicios registrados");
                }
            }

            historial.add(
                    new HistorialProgresoDetalleDTO(
                            progreso.getIdProgreso(),
                            progreso.getFechaRegistro(),
                            nombreRutina,
                            ejercicios,
                            progreso.getPorcentajeCumplimiento(),
                            progreso.getPesoActual(),
                            progreso.getObservaciones()
                    )
            );
        }

        return new HistorialProgresoDTO(
                idUsuario,
                true,
                historial.size(),
                historial,
                "Historial de progreso obtenido correctamente."
        );
    }
}
