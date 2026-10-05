package com.trueform.backend.servicios;

import com.trueform.backend.dto.ComparacionMensualDTO;
import com.trueform.backend.dto.ResumenProgresoDTO;
import com.trueform.backend.entidades.Progreso;
import com.trueform.backend.repositorios.ProgresoRepositorio;
import com.trueform.backend.util.CalculosProgreso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
public class InformeProgresoServicio {
    @Autowired
    private ProgresoRepositorio repositorio;
    @Autowired
    private AccesoServicio acceso;

    public ComparacionMensualDTO comparar(Long idUsuario, YearMonth mesBase, YearMonth mesComparado) {
        acceso.verificarUsuario(idUsuario);
        if (mesBase.equals(mesComparado)) {
            throw new IllegalArgumentException("Selecciona dos meses diferentes.");
        }
        ResumenProgresoDTO base = resumen(idUsuario, mesBase.toString(), repositorio
                .findByUsuario_IdUsuarioAndFechaRegistroGreaterThanEqualAndFechaRegistroLessThanOrderByFechaRegistroAsc(
                        idUsuario, mesBase.atDay(1).atStartOfDay(), mesBase.plusMonths(1).atDay(1).atStartOfDay()));
        ResumenProgresoDTO comparado = resumen(idUsuario, mesComparado.toString(), repositorio
                .findByUsuario_IdUsuarioAndFechaRegistroGreaterThanEqualAndFechaRegistroLessThanOrderByFechaRegistroAsc(
                        idUsuario, mesComparado.atDay(1).atStartOfDay(), mesComparado.plusMonths(1).atDay(1).atStartOfDay()));
        ComparacionMensualDTO resultado = new ComparacionMensualDTO();
        resultado.setIdUsuario(idUsuario);
        resultado.setMeses(List.of(base, comparado));
        boolean pesoComparable = base.getRegistrosConPeso() > 0 && comparado.getRegistrosConPeso() > 0;
        boolean cumplimientoComparable = base.getRegistrosConCumplimiento() > 0 && comparado.getRegistrosConCumplimiento() > 0;
        resultado.setHayDatos(pesoComparable || cumplimientoComparable);
        if (!resultado.isHayDatos()) {
            resultado.setMensaje("Necesitas al menos un registro de la misma métrica (peso o cumplimiento) en cada mes para realizar la comparación.");
            return resultado;
        }
        resultado.setDiferenciaPesoPromedioKg(CalculosProgreso.diferencia(base.getPesoPromedioKg(), comparado.getPesoPromedioKg()));
        resultado.setDiferenciaCumplimientoPuntos(CalculosProgreso.diferencia(base.getCumplimientoPromedio(), comparado.getCumplimientoPromedio()));
        resultado.setMensaje("Comparación obtenida. Las diferencias representan el segundo mes menos el primero.");
        return resultado;
    }

    public ResumenProgresoDTO obtenerResumen(Long idUsuario) {
        acceso.verificarUsuario(idUsuario);
        return resumen(idUsuario, "Historial completo", repositorio.findByUsuario_IdUsuario(idUsuario));
    }

    private ResumenProgresoDTO resumen(Long idUsuario, String periodo, List<Progreso> progresos) {
        ResumenProgresoDTO dto = new ResumenProgresoDTO();
        dto.setIdUsuario(idUsuario);
        dto.setPeriodo(periodo);
        BigDecimal sumaPeso = BigDecimal.ZERO;
        BigDecimal sumaCumplimiento = BigDecimal.ZERO;
        int conPeso = 0;
        int conCumplimiento = 0;
        int relevantes = 0;
        LocalDateTime primeraFechaPeso = null;
        LocalDateTime ultimaFechaPeso = null;
        Long primerIdPeso = null;
        Long ultimoIdPeso = null;
        for (Progreso progreso : progresos) {
            if (progreso.getFechaRegistro() == null ||
                    (progreso.getPesoActual() == null && progreso.getPorcentajeCumplimiento() == null)) continue;
            relevantes++;
            LocalDateTime fecha = progreso.getFechaRegistro();
            if (dto.getDesde() == null || fecha.isBefore(dto.getDesde())) dto.setDesde(fecha);
            if (dto.getHasta() == null || fecha.isAfter(dto.getHasta())) dto.setHasta(fecha);
            if (progreso.getPesoActual() != null) {
                sumaPeso = sumaPeso.add(progreso.getPesoActual());
                conPeso++;
                Long id = progreso.getIdProgreso() == null ? 0L : progreso.getIdProgreso();
                if (primeraFechaPeso == null || fecha.isBefore(primeraFechaPeso) ||
                        (fecha.equals(primeraFechaPeso) && id < primerIdPeso)) {
                    primeraFechaPeso = fecha;
                    primerIdPeso = id;
                    dto.setPesoInicialKg(progreso.getPesoActual());
                }
                if (ultimaFechaPeso == null || fecha.isAfter(ultimaFechaPeso) ||
                        (fecha.equals(ultimaFechaPeso) && id > ultimoIdPeso)) {
                    ultimaFechaPeso = fecha;
                    ultimoIdPeso = id;
                    dto.setPesoFinalKg(progreso.getPesoActual());
                }
            }
            if (progreso.getPorcentajeCumplimiento() != null) {
                sumaCumplimiento = sumaCumplimiento.add(progreso.getPorcentajeCumplimiento());
                conCumplimiento++;
            }
        }
        dto.setRegistrosProgreso(relevantes);
        dto.setRegistrosConPeso(conPeso);
        dto.setRegistrosConCumplimiento(conCumplimiento);
        dto.setPesoPromedioKg(CalculosProgreso.promedio(sumaPeso, conPeso));
        dto.setCumplimientoPromedio(CalculosProgreso.promedio(sumaCumplimiento, conCumplimiento));
        dto.setHayDatos(relevantes > 0);
        dto.setMensaje(dto.isHayDatos() ? "Resumen obtenido correctamente." : "No hay datos suficientes para exportar el resumen. Registra tu peso o cumplimiento.");
        return dto;
    }

    public String generarCsv(ResumenProgresoDTO dto) {
        // UTF-8 BOM para Excel
        return "\uFEFFid_usuario;desde;hasta;registros_progreso;registros_con_peso;registros_con_cumplimiento;peso_promedio_kg;peso_inicial_kg;peso_final_kg;cumplimiento_promedio_pct\r\n"
                + dto.getIdUsuario() + ";" + CalculosProgreso.celda(dto.getDesde()) + ";"
                + CalculosProgreso.celda(dto.getHasta()) + ";" + dto.getRegistrosProgreso() + ";"
                + dto.getRegistrosConPeso() + ";" + dto.getRegistrosConCumplimiento() + ";"
                + CalculosProgreso.celda(dto.getPesoPromedioKg()) + ";"
                + CalculosProgreso.celda(dto.getPesoInicialKg()) + ";"
                + CalculosProgreso.celda(dto.getPesoFinalKg()) + ";"
                + CalculosProgreso.celda(dto.getCumplimientoPromedio()) + "\r\n";
    }
}
