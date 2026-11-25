
package com.trueform.backend.config;

import com.trueform.backend.security.filters.JwtRequestFilter;
import com.trueform.backend.security.servicios.CustomUserDetailsService;
import com.trueform.backend.security.util.JwtUtil;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public SecurityConfig(
            CustomUserDetailsService userDetailsService,
            JwtUtil jwtUtil) {

        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        return http
                .cors(Customizer.withDefaults())

                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )


                .authorizeHttpRequests(auth -> auth

                        // Permitir las solicitudes CORS previas
                        .requestMatchers(HttpMethod.OPTIONS, "/**")
                        .permitAll()

			.requestMatchers(
       				 "/swagger-ui/**",
        			"/swagger-ui.html",
        			"/v3/api-docs",
       				"/v3/api-docs/**"
			).permitAll()
                        // Login JWT y registro de nuevos usuarios
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/auth/login-jwt",
                                "/api/usuarios"
                        ).permitAll()

                        // Deshabilitar el login antiguo sin JWT
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/auth/login"
                        ).denyAll()

                        // Catálogos y recursos educativos públicos
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/objetivos/**",
                                "/api/ejercicios/**",
                                "/api/glosario/buscar",
                                "/api/tips/hoy"
                        ).permitAll()

                        // Toda la administración exige rol ADMIN
                        .requestMatchers("/api/admin/**")
                        .hasRole("ADMIN")

                        // Todo lo demás requiere autenticación JWT
                        .anyRequest().authenticated()
                )


                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(
                                (request, response, exception) ->
                                        response.sendError(
                                                HttpServletResponse.SC_UNAUTHORIZED
                                        )
                        )
                        .accessDeniedHandler(
                                (request, response, exception) ->
                                        response.sendError(
                                                HttpServletResponse.SC_FORBIDDEN
                                        )
                        )
                )

                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)

                .addFilterBefore(
                        new JwtRequestFilter(
                                userDetailsService,
                                jwtUtil
                        ),
                        UsernamePasswordAuthenticationFilter.class
                )

                .build();
    }
}
