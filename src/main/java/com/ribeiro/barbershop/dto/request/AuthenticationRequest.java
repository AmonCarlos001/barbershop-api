package com.ribeiro.barbershop.dto.request;

/** Record contendo as credenciais básicas necessárias para a operação de login. */

public record AuthenticationRequest(
        String email,
        String senha
) {
}
