package com.trueform.backend;

import com.trueform.backend.entidades.Usuario;
import com.trueform.backend.entidades.Rutina;
import com.trueform.backend.repositorios.UsuarioRepositorio;
import com.trueform.backend.repositorios.RutinaRepositorio;
import com.trueform.backend.servicios.UsuarioServicio;
import com.trueform.backend.servicios.RutinaServicio;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Pruebas unitarias de los listados sin contexto de seguridad.
class ServiciosSinSecurityTests {
    @Test
    void usuariosListaSinRequerirPrincipalAutenticado() {
        UsuarioRepositorio repositorio = mock(UsuarioRepositorio.class);
        UsuarioServicio servicio = new UsuarioServicio();
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        when(repositorio.findAll()).thenReturn(List.of(usuario));
        ReflectionTestUtils.setField(servicio, "usuarioRepositorio", repositorio);
        assertEquals(1L, servicio.listar().get(0).getIdUsuario());
        verify(repositorio).findAll();
    }

    @Test
    void rutinasListaSinRequerirPrincipalAutenticado() {
        RutinaRepositorio repositorio = mock(RutinaRepositorio.class);
        RutinaServicio servicio = new RutinaServicio();
        when(repositorio.findAll()).thenReturn(List.of(new Rutina()));
        ReflectionTestUtils.setField(servicio, "rutinaRepositorio", repositorio);
        assertEquals(1, servicio.listar().size());
        verify(repositorio).findAll();
    }
}
