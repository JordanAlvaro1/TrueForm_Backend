package com.trueform.backend.servicios;

import com.trueform.backend.entidades.Objetivo;
import com.trueform.backend.repositorios.ObjetivoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ObjetivoServicio {
    @Autowired
    private ObjetivoRepositorio objetivoRepositorio;

    public List<Objetivo> listar() {
        return objetivoRepositorio.findAll();
    }

    public Objetivo buscarPorId(Long id) {
        return objetivoRepositorio.findById(id).orElse(null);
    }

    public Objetivo registrar(Objetivo objetivo) {
        return objetivoRepositorio.save(objetivo);
    }

    public Objetivo modificar(Objetivo objetivo) {
        return objetivoRepositorio.save(objetivo);
    }

    public void eliminar(Long id) {
        objetivoRepositorio.deleteById(id);
    }
}
