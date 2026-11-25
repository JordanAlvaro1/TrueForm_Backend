
package com.trueform.backend.dto;

public record JwtLoginResponseDTO(
        String token,
        String rol,
        UsuarioDTO usuario
) {
}
