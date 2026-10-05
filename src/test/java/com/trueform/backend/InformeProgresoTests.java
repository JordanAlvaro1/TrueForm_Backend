package com.trueform.backend;

import com.trueform.backend.dto.*;
import com.trueform.backend.entidades.Progreso;
import com.trueform.backend.repositorios.ProgresoRepositorio;
import com.trueform.backend.servicios.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InformeProgresoTests {
    private final ProgresoRepositorio repo = mock(ProgresoRepositorio.class);
    private final AccesoServicio acceso = mock(AccesoServicio.class);
    private final InformeProgresoServicio servicio = new InformeProgresoServicio();

    @BeforeEach void preparar() {
        ReflectionTestUtils.setField(servicio, "repositorio", repo);
        ReflectionTestUtils.setField(servicio, "acceso", acceso);
    }
    private Progreso dato(long id, String fecha, String peso, String cumplimiento) {
        Progreso p = new Progreso();
        p.setIdProgreso(id);
        p.setFechaRegistro(LocalDateTime.parse(fecha));
        p.setPesoActual(peso == null ? null : new BigDecimal(peso));
        p.setPorcentajeCumplimiento(cumplimiento == null ? null : new BigDecimal(cumplimiento));
        return p;
    }
    private void mes(String valor, List<Progreso> datos) {
        YearMonth mes = YearMonth.parse(valor);
        when(repo.findByUsuario_IdUsuarioAndFechaRegistroGreaterThanEqualAndFechaRegistroLessThanOrderByFechaRegistroAsc(
                1L, mes.atDay(1).atStartOfDay(), mes.plusMonths(1).atDay(1).atStartOfDay())).thenReturn(datos);
    }
    @Test void comparaMesesConValoresFaltantesYLimitesExactos() {
        mes("2026-08", List.of(dato(1, "2026-08-01T00:00:00", "80", "0"),
                dato(2, "2026-08-31T23:59:59", null, "100")));
        mes("2026-09", List.of(dato(3, "2026-09-01T00:00:00", "78", "80")));
        ComparacionMensualDTO r = servicio.comparar(1L, YearMonth.of(2026,8), YearMonth.of(2026,9));
        assertTrue(r.isHayDatos());
        assertEquals(new BigDecimal("-2.00"), r.getDiferenciaPesoPromedioKg());
        assertEquals(new BigDecimal("30.00"), r.getDiferenciaCumplimientoPuntos());
        assertEquals(1, r.getMeses().get(0).getRegistrosConPeso());
        verify(acceso).verificarUsuario(1L);
        verify(repo).findByUsuario_IdUsuarioAndFechaRegistroGreaterThanEqualAndFechaRegistroLessThanOrderByFechaRegistroAsc(
                1L, LocalDateTime.of(2026,8,1,0,0), LocalDateTime.of(2026,9,1,0,0));
    }
    @Test void rechazaMesVacioOMetricasSinCoincidencia() {
        mes("2026-08", List.of(dato(1, "2026-08-01T00:00:00", "80", null)));
        mes("2026-09", List.of());
        assertFalse(servicio.comparar(1L, YearMonth.of(2026,8), YearMonth.of(2026,9)).isHayDatos());
        mes("2026-09", List.of(dato(2, "2026-09-01T00:00:00", null, "90")));
        assertFalse(servicio.comparar(1L, YearMonth.of(2026,8), YearMonth.of(2026,9)).isHayDatos());
    }
    @Test void noConfundeAusenciaConCero() {
        when(repo.findByUsuario_IdUsuario(1L)).thenReturn(List.of(dato(1,"2026-08-01T00:00:00",null,"0")));
        ResumenProgresoDTO r = servicio.obtenerResumen(1L);
        assertTrue(r.isHayDatos());
        assertNull(r.getPesoPromedioKg());
        assertEquals(new BigDecimal("0.00"), r.getCumplimientoPromedio());
    }
    @Test void resumenVacioIncluyeMensaje() {
        when(repo.findByUsuario_IdUsuario(1L)).thenReturn(List.of(dato(1,"2026-08-01T00:00:00",null,null)));
        ResumenProgresoDTO r = servicio.obtenerResumen(1L);
        assertFalse(r.isHayDatos());
        assertTrue(r.getMensaje().contains("No hay datos suficientes"));
    }
    @Test void exportaResumenCronologicoSinDependerDelOrdenDelRepositorio() {
        when(repo.findByUsuario_IdUsuario(1L)).thenReturn(List.of(
                dato(2,"2026-09-01T00:00:00","78","100"), dato(1,"2026-08-01T00:00:00","80","50")));
        ResumenProgresoDTO r = servicio.obtenerResumen(1L);
        assertEquals(new BigDecimal("80"), r.getPesoInicialKg());
        assertEquals(new BigDecimal("78"), r.getPesoFinalKg());
        assertEquals(new BigDecimal("79.00"), r.getPesoPromedioKg());
        String csv = servicio.generarCsv(r);
        assertTrue(csv.startsWith("\uFEFFid_usuario;"));
        assertTrue(csv.endsWith(";79.00;80;78;75.00\r\n"));
        assertEquals(2, csv.split("\r\n").length);
    }
    @Test void rechazaMesesIguales() {
        assertThrows(IllegalArgumentException.class, () -> servicio.comparar(1L, YearMonth.of(2026,8), YearMonth.of(2026,8)));
        verifyNoInteractions(repo);
    }
    @Test void noConsultaDatosSiElUsuarioNoTienePermiso() {
        doThrow(new org.springframework.security.access.AccessDeniedException("No permitido")).when(acceso).verificarUsuario(2L);
        assertThrows(org.springframework.security.access.AccessDeniedException.class, () -> servicio.obtenerResumen(2L));
        verifyNoInteractions(repo);
    }
}
