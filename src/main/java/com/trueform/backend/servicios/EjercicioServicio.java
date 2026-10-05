package com.trueform.backend.servicios;

import com.trueform.backend.entidades.Ejercicio;
import com.trueform.backend.repositorios.EjercicioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EjercicioServicio {
    @Autowired
    private EjercicioRepositorio ejercicioRepositorio;

    public List<Ejercicio> listar() {
        return ejercicioRepositorio.findAll();
    }

    public Ejercicio buscarPorId(Long id) {
        return ejercicioRepositorio.findById(id).orElse(null);
    }

    public Ejercicio registrar(Ejercicio ejercicio) {
        return ejercicioRepositorio.save(ejercicio);
    }

    public Ejercicio modificar(Ejercicio ejercicio) {
        return ejercicioRepositorio.save(ejercicio);
    }

    public void eliminar(Long id) {
        ejercicioRepositorio.deleteById(id);
    }

    public List<Ejercicio> buscarPorGrupoMuscular(String grupoMuscular) {
        return ejercicioRepositorio.findByGrupoMuscular(grupoMuscular);
    }
}
