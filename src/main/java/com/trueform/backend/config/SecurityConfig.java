package com.trueform.backend.config;

import com.trueform.backend.repositorios.UsuarioRepositorio;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
public class SecurityConfig {
    @Bean
    public UserDetailsService userDetailsService(UsuarioRepositorio repositorio) {
        return email -> {
            com.trueform.backend.entidades.Usuario usuario = repositorio
                    .findByEmailIgnoreCase(email.trim())
                    .orElseThrow(() -> new UsernameNotFoundException("Credenciales incorrectas"));
            String rol = "ADMIN".equals(usuario.getRol()) ? "ADMIN" : "USER";
            return new UsuarioAutenticado(usuario.getIdUsuario(), usuario.getEmail(), usuario.getPasswordHash(), rol);
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
            UserDetailsService usuarios, PasswordEncoder encoder) throws Exception {
        DaoAuthenticationProvider proveedor = new DaoAuthenticationProvider(usuarios);
        proveedor.setPasswordEncoder(encoder);
        http.authenticationProvider(proveedor)
            .cors(Customizer.withDefaults())
            .securityContext(context -> context.securityContextRepository(
                new org.springframework.security.web.context.RequestAttributeSecurityContextRepository()))
            // Se conserva la protección CSRF predeterminada de Spring.
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/error", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/auth/csrf", "/api/objetivos", "/api/objetivos/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/usuarios").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/ejercicios", "/api/ejercicios/**").authenticated()
                .requestMatchers("/api/objetivos", "/api/objetivos/**", "/api/ejercicios", "/api/ejercicios/**").hasRole("ADMIN")
                .anyRequest().authenticated())
            .httpBasic(Customizer.withDefaults())
            .formLogin(form -> form.disable())
            .logout(logout -> logout.disable());
        return http.build();
    }
}
