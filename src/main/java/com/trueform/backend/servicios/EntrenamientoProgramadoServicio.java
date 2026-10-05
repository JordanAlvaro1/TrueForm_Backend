package com.trueform.backend.servicios;

import com.trueform.backend.entidades.EntrenamientoProgramado;
import com.trueform.backend.repositorios.EntrenamientoProgramadoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDate;
import com.trueform.backend.dto.ResumenSemanalDTO;

@Service
public class EntrenamientoProgramadoServicio {
    @Autowired
    private AccesoServicio acceso;
    @Autowired
    private EntrenamientoProgramadoRepositorio repositorio;

    public EntrenamientoProgramado buscarPorId(Long id) {
        EntrenamientoProgramado encontrado = repositorio.findById(id).orElse(null);
        if (encontrado != null) acceso.verificarUsuario(encontrado.getRutina().getUsuario().getIdUsuario());
        return encontrado;
    }
    public EntrenamientoProgramado registrar(EntrenamientoProgramado entidad) {
        acceso.verificarUsuario(entidad.getRutina().getUsuario().getIdUsuario());
        return repositorio.save(entidad);
    }
    public EntrenamientoProgramado modificar(EntrenamientoProgramado entidad) {
        acceso.verificarUsuario(entidad.getRutina().getUsuario().getIdUsuario());
        buscarPorId(entidad.getIdProgramacion());
        return repositorio.save(entidad);
    }
    public void eliminar(Long id) {
        buscarPorId(id);
        repositorio.deleteById(id);
    }
    public List<EntrenamientoProgramado> buscarPorUsuario(Long idUsuario) {
        acceso.verificarUsuario(idUsuario);
        return repositorio.findByRutina_Usuario_IdUsuarioOrderByFechaAsc(idUsuario);
    }
    public ResumenSemanalDTO resumenSemanal(Long idUsuario, LocalDate fecha) {
        acceso.verificarUsuario(idUsuario);
        LocalDate inicio = fecha.minusDays(fecha.getDayOfWeek().getValue() - 1);
        LocalDate fin = inicio.plusDays(6);
        int completados = 0;
        int pendientes = 0;
        int cancelados = 0;
        for (EntrenamientoProgramado entrenamiento : buscarPorUsuario(idUsuario)) {
            if (!entrenamiento.getFecha().isBefore(inicio) && !entrenamiento.getFecha().isAfter(fin)) {
                if (entrenamiento.getEstado().equals("completado")) completados++;
                if (entrenamiento.getEstado().equals("pendiente")) pendientes++;
                if (entrenamiento.getEstado().equals("cancelado")) cancelados++;
            }
        }
        int programados = completados + pendientes;
        double porcentaje = 0;
        if (programados > 0) porcentaje = Math.round(completados * 10000.0 / programados) / 100.0;
        ResumenSemanalDTO resumen = new ResumenSemanalDTO();
        resumen.setIdUsuario(idUsuario);
        resumen.setFechaInicio(inicio);
        resumen.setFechaFin(fin);
        resumen.setProgramados(programados);
        resumen.setCompletados(completados);
        resumen.setPendientes(pendientes);
        resumen.setCancelados(cancelados);
        resumen.setPorcentajeCumplimiento(porcentaje);
        return resumen;
    }
}
