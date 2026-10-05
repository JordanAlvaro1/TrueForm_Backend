package com.trueform.backend.servicios;

import com.trueform.backend.dto.AjusteRutinaDTO;
import com.trueform.backend.dto.DiaRutinaDTO;
import com.trueform.backend.dto.EjercicioSemanalDTO;
import com.trueform.backend.dto.RutinaSemanalDTO;
import com.trueform.backend.entidades.Progreso;
import com.trueform.backend.entidades.Rutina;
import com.trueform.backend.entidades.RutinaEjercicio;
import com.trueform.backend.repositorios.ProgresoRepositorio;
import com.trueform.backend.repositorios.RutinaEjercicioRepositorio;
import com.trueform.backend.repositorios.RutinaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class RutinaServicio {
    @Autowired
    private AccesoServicio acceso;
    @Autowired
    private
    RutinaRepositorio rutinaRepositorio;

    @Autowired
    private RutinaEjercicioRepositorio rutinaEjercicioRepositorio;

    @Autowired
    private ProgresoRepositorio progresoRepositorio;

    public List<Rutina> listar() {
        return rutinaRepositorio.findByUsuario_IdUsuario(acceso.idActual());
    }

    public Rutina buscarPorId(Long id) {
        Rutina encontrado = rutinaRepositorio.findById(id).orElse(null);
        if (encontrado != null) acceso.verificarUsuario(encontrado.getUsuario().getIdUsuario());
        return encontrado;
    }

    public Rutina registrar(Rutina rutina) {
        acceso.verificarUsuario(rutina.getUsuario().getIdUsuario());
        return rutinaRepositorio.save(rutina);
    }

    public Rutina modificar(Rutina rutina) {
        acceso.verificarUsuario(rutina.getUsuario().getIdUsuario());
        buscarPorId(rutina.getIdRutina());
        return rutinaRepositorio.save(rutina);
    }

    public void eliminar(Long id) {
        buscarPorId(id);
        rutinaRepositorio.deleteById(id);
    }

    public List<Rutina> buscarPorUsuario(Long idUsuario) {
        acceso.verificarUsuario(idUsuario);
        return rutinaRepositorio.findByUsuario_IdUsuario(idUsuario);
    }
    public RutinaSemanalDTO obtenerRutinaSemanal(Long idUsuario) {
        acceso.verificarUsuario(idUsuario);

        List<Rutina> rutinas =
                rutinaRepositorio.findByUsuario_IdUsuario(idUsuario);

        if (rutinas.isEmpty()) {
            return null;
        }

        // Buscar primero una rutina activa
        Rutina rutina = rutinas.stream()
                .filter(r -> "ACTIVA".equalsIgnoreCase(r.getEstado()))
                .findFirst()
                .orElse(rutinas.get(0));

        List<RutinaEjercicio> rutinaEjercicios =
                rutinaEjercicioRepositorio
                        .findByRutina_IdRutinaOrderByOrdenEjercicioAsc(
                                rutina.getIdRutina());

        List<String> diasSemana = Arrays.asList(
                "LUNES",
                "MARTES",
                "MIERCOLES",
                "JUEVES",
                "VIERNES",
                "SABADO",
                "DOMINGO"
        );

        List<DiaRutinaDTO> dias = new ArrayList<>();


        for (String dia : diasSemana) {

            List<RutinaEjercicio> ejerciciosDia =
                    rutinaEjercicios.stream()
                            .filter(re ->
                                    re.getDiaSemana() != null &&
                                            re.getDiaSemana()
                                                    .equalsIgnoreCase(dia))
                            .toList();


            // Si no existen ejercicios, es día de descanso
            if (ejerciciosDia.isEmpty()) {

                DiaRutinaDTO diaDescanso =
                        new DiaRutinaDTO();

                diaDescanso.setDia(dia);
                diaDescanso.setGrupoMuscular(null);
                diaDescanso.setDescanso(true);
                diaDescanso.setMensaje(
                        "Día de recuperación. Mantente hidratado y realiza movilidad ligera."
                );
                diaDescanso.setEjercicios(new ArrayList<>());

                dias.add(diaDescanso);

                continue;
            }


            List<EjercicioSemanalDTO> ejercicios =
                    new ArrayList<>();


            for (RutinaEjercicio re : ejerciciosDia) {

                EjercicioSemanalDTO ejercicioDTO =
                        new EjercicioSemanalDTO();

                ejercicioDTO.setIdEjercicio(
                        re.getEjercicio().getIdEjercicio());

                ejercicioDTO.setNombreEjercicio(
                        re.getEjercicio().getNombreEjercicio());

                ejercicioDTO.setGrupoMuscular(
                        re.getEjercicio().getGrupoMuscular());

                ejercicioDTO.setSeries(
                        re.getSeries());

                ejercicioDTO.setRepeticiones(
                        re.getRepeticiones());

                ejercicioDTO.setDescansoSegundos(
                        re.getDescansoSegundos());

                ejercicioDTO.setOrdenEjercicio(
                        re.getOrdenEjercicio());

                ejercicios.add(ejercicioDTO);
            }


            DiaRutinaDTO diaRutina =
                    new DiaRutinaDTO();

            diaRutina.setDia(dia);

            diaRutina.setGrupoMuscular(
                    ejerciciosDia.get(0)
                            .getEjercicio()
                            .getGrupoMuscular());

            diaRutina.setDescanso(false);

            diaRutina.setMensaje("Entrenamiento programado");
            diaRutina.setEjercicios(ejercicios);
            dias.add(diaRutina);
        }


        RutinaSemanalDTO respuesta = new RutinaSemanalDTO();

        respuesta.setIdRutina(rutina.getIdRutina());

        respuesta.setIdUsuario(rutina.getUsuario().getIdUsuario());

        respuesta.setNombreRutina(rutina.getNombreRutina());

        respuesta.setDescripcion(rutina.getDescripcion());

        respuesta.setDias(dias);

        return respuesta;
    }

    // HU11
    public AjusteRutinaDTO evaluarAjusteRutina(Long idUsuario) {
        acceso.verificarUsuario(idUsuario);

        List<Rutina> rutinas =
                rutinaRepositorio.findByUsuario_IdUsuario(idUsuario);

        if (rutinas.isEmpty()) {
            return null;
        }

        // Buscar primero la rutina activa
        Rutina rutina = rutinas.stream()
                .filter(r -> "ACTIVA".equalsIgnoreCase(r.getEstado()))
                .findFirst()
                .orElse(rutinas.get(0));

        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime haceDosSemanas = ahora.minusWeeks(2);

        List<Progreso> progresos =
                progresoRepositorio
                        .findByUsuario_IdUsuarioAndFechaRegistroBetweenOrderByFechaRegistroAsc(
                                idUsuario,
                                haceDosSemanas,
                                ahora
                        );

        // Verificar que existan registros en las dos semanas
        LocalDateTime inicioSemanaActual = ahora.minusWeeks(1);

        List<Progreso> semanaAnterior = progresos.stream()
                .filter(p -> p.getFechaRegistro().isBefore(inicioSemanaActual))
                .toList();

        List<Progreso> semanaActual = progresos.stream()
                .filter(p -> !p.getFechaRegistro().isBefore(inicioSemanaActual))
                .toList();

        if (semanaAnterior.isEmpty() || semanaActual.isEmpty()) {

            return new AjusteRutinaDTO(
                    idUsuario,
                    rutina.getIdRutina(),
                    false,
                    false,
                    null,
                    "Se requieren al menos 2 semanas de progreso para evaluar un ajuste de rutina."
            );
        }

        BigDecimal promedioAnterior = calcularPromedio(semanaAnterior);
        BigDecimal promedioActual = calcularPromedio(semanaActual);


        BigDecimal mejora =
                promedioActual.subtract(promedioAnterior);

        boolean estancamiento =
                mejora.compareTo(new BigDecimal("5")) < 0;

        if (estancamiento) {

            return new AjusteRutinaDTO(
                    idUsuario,
                    rutina.getIdRutina(),
                    true,
                    true,
                    "Aumentar progresivamente la intensidad de la rutina.",
                    "Se detectó estancamiento en tu progreso."
            );
        }

        return new AjusteRutinaDTO(
                idUsuario,
                rutina.getIdRutina(),
                true,
                false,
                "Mantener la rutina actual y continuar con la progresión.",
                "Tu progreso es favorable. No es necesario ajustar la rutina por el momento."
        );
    }


    // HU11 - Calcular promedio de cumplimiento
    private BigDecimal calcularPromedio(List<Progreso> progresos) {

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
            return BigDecimal.ZERO;
        }

        return suma.divide(
                BigDecimal.valueOf(cantidad),
                2,
                java.math.RoundingMode.HALF_UP
        );
    }
}
