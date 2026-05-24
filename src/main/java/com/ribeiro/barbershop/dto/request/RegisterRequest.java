package com.ribeiro.barbershop.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Record utilizado para receber e validar os dados cadastrais de um novo usuário. */

public record RegisterRequest(
        @NotBlank(message = "O nome é obrigatório.")
        String nome,

        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "Por favor, insira um e-mail em formato válido.")
        String email,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 3, message = "A senha deve ter no mínimo 3 caracteres.")
        String senha
) {
}
