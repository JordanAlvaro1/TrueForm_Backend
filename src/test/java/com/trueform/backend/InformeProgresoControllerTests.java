package com.trueform.backend;

import com.trueform.backend.controladores.InformeProgresoController;
import com.trueform.backend.dto.ResumenProgresoDTO;
import com.trueform.backend.entidades.Usuario;
import com.trueform.backend.servicios.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// El contrato HTTP se prueba aparte de la cadena de filtros de SecurityTests.
class InformeProgresoControllerTests {
    private final InformeProgresoServicio servicio = mock(InformeProgresoServicio.class);
    private final UsuarioServicio usuarios = mock(UsuarioServicio.class);
    private MockMvc mvc;
    @BeforeEach void iniciar() {
        InformeProgresoController controller = new InformeProgresoController();
        ReflectionTestUtils.setField(controller, "informes", servicio);
        ReflectionTestUtils.setField(controller, "usuarios", usuarios);
        when(usuarios.buscarPorId(1L)).thenReturn(new Usuario());
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }
    @Test void mesesInvalidosOIgualesDevuelven400() throws Exception {
        for (String mes : new String[]{"2026-13", "agosto", "2026-09"}) {
            mvc.perform(get("/api/progresos/usuario/1/comparacion-mensual")
                    .param("mesBase", mes).param("mesComparado", "2026-09"))
                    .andExpect(status().isBadRequest()).andExpect(jsonPath("$.mensaje").isNotEmpty());
        }
        verifyNoInteractions(servicio);
    }
    @Test void exportacionSinDatosDevuelveJsonSinAdjunto() throws Exception {
        ResumenProgresoDTO resumen = new ResumenProgresoDTO();
        resumen.setHayDatos(false);
        resumen.setMensaje("No hay datos suficientes");
        when(servicio.obtenerResumen(1L)).thenReturn(resumen);
        mvc.perform(get("/api/progresos/usuario/1/exportar"))
                .andExpect(status().is(422))
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(header().doesNotExist("Content-Disposition"))
                .andExpect(jsonPath("$.hayDatos").value(false));
        verify(servicio, never()).generarCsv(any());
    }
    @Test void exportacionConDatosDevuelveArchivo() throws Exception {
        ResumenProgresoDTO resumen = new ResumenProgresoDTO();
        resumen.setHayDatos(true);
        when(servicio.obtenerResumen(1L)).thenReturn(resumen);
        when(servicio.generarCsv(resumen)).thenReturn("\uFEFFid_usuario;registros\r\n1;2\r\n");
        mvc.perform(get("/api/progresos/usuario/1/exportar"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("text/csv"))
                .andExpect(header().string("Content-Disposition", "attachment; filename=progreso-1.csv"))
                .andExpect(content().string("\uFEFFid_usuario;registros\r\n1;2\r\n"));
    }
    @Test void usuarioInexistenteDevuelve404() throws Exception {
        mvc.perform(get("/api/progresos/usuario/999/resumen")).andExpect(status().isNotFound());
        verifyNoInteractions(servicio);
    }
}
