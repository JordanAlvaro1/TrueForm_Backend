package com.trueform.backend.config;

import com.trueform.backend.dto.UsuarioDTO;
import com.trueform.backend.entidades.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(org.modelmapper.convention.MatchingStrategies.STRICT);

        modelMapper.typeMap(Usuario.class, UsuarioDTO.class)
                .addMapping(
                        src -> src.getObjetivo().getIdObjetivo(),
                        UsuarioDTO::setIdObjetivo
                );

        return modelMapper;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
