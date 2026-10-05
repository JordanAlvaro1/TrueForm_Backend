package com.trueform.backend;

import com.trueform.backend.config.SecurityConfig;
import com.trueform.backend.controladores.CsrfController;
import com.trueform.backend.entidades.Usuario;
import com.trueform.backend.repositorios.*;
import com.trueform.backend.servicios.AccesoServicio;
import org.junit.jupiter.api.*;
import org.springframework.context.annotation.*;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SecurityTests {
    private AnnotationConfigWebApplicationContext context;
    private MockMvc mvc;
    @BeforeEach void iniciar() {
        context = new AnnotationConfigWebApplicationContext();
        context.setServletContext(new MockServletContext());
        context.register(Configuracion.class);
        context.refresh();
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(context.getBean(FilterChainProxy.class)).build();
    }
    @AfterEach void cerrar() { context.close(); }

    @Configuration
    @EnableWebMvc
    @Import({SecurityConfig.class, AccesoServicio.class, CsrfController.class, ControladorPrueba.class})
    static class Configuracion {
        @Bean PasswordEncoder encoder() { return new BCryptPasswordEncoder(4); }
        @Bean RutinaRepositorio rutinas() { return mock(RutinaRepositorio.class); }
        @Bean UsuarioRepositorio usuarios(PasswordEncoder encoder) {
            UsuarioRepositorio repo = mock(UsuarioRepositorio.class);
            Usuario uno = new Usuario();
            uno.setIdUsuario(1L); uno.setEmail("uno@example.com");
            uno.setPasswordHash(encoder.encode("clave")); uno.setRol(null);
            Usuario admin = new Usuario();
            admin.setIdUsuario(2L); admin.setEmail("admin@example.com");
            admin.setPasswordHash(encoder.encode("clave")); admin.setRol("ADMIN");
            when(repo.findByEmailIgnoreCase("uno@example.com")).thenReturn(Optional.of(uno));
            when(repo.findByEmailIgnoreCase("admin@example.com")).thenReturn(Optional.of(admin));
            return repo;
        }
    }
    @RestController
    static class ControladorPrueba {
        private final AccesoServicio acceso;
        ControladorPrueba(AccesoServicio acceso) { this.acceso = acceso; }
        @GetMapping("/api/usuarios/{id}") String usuario(@PathVariable Long id) {
            acceso.verificarUsuario(id); return "ok";
        }
        @PostMapping("/api/ejercicios") String catalogo() { return "ok"; }
    }
    @Test void exigeCredencialesEnCadaConsulta() throws Exception {
        mvc.perform(get("/api/usuarios/1")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/usuarios/1").with(httpBasic("uno@example.com","incorrecta"))).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/usuarios/1").with(httpBasic("uno@example.com","clave"))).andExpect(status().isOk());
        mvc.perform(get("/api/usuarios/1")).andExpect(status().isUnauthorized());
    }
    @Test void impideAccesoAOtroUsuarioInclusoParaAdministrador() throws Exception {
        mvc.perform(get("/api/usuarios/2").with(httpBasic("uno@example.com","clave"))).andExpect(status().isForbidden());
        mvc.perform(get("/api/usuarios/1").with(httpBasic("admin@example.com","clave"))).andExpect(status().isForbidden());
    }
    @Test void catalogoExigeAdministradorYCsrf() throws Exception {
        mvc.perform(post("/api/ejercicios").with(httpBasic("admin@example.com","clave"))).andExpect(status().isForbidden());
        mvc.perform(post("/api/ejercicios").with(csrf()).with(httpBasic("uno@example.com","clave"))).andExpect(status().isForbidden());
        mvc.perform(post("/api/ejercicios").with(csrf()).with(httpBasic("admin@example.com","clave"))).andExpect(status().isOk());
    }
    @Test void permiteObtenerTokenCsrfAntesDelRegistro() throws Exception {
        mvc.perform(get("/api/auth/csrf")).andExpect(status().isOk()).andExpect(jsonPath("$.token").isNotEmpty());
    }
}
