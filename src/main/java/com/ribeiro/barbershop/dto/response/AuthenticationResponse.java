package com.ribeiro.barbershop.dto.response;

/** Record utilizado para retornar o token JWT gerado após uma autenticação bem-sucedida. */

public record AuthenticationResponse(
        String token
) {
}
