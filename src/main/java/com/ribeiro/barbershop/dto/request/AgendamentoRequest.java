package com.ribeiro.barbershop.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/** Record utilizado para receber e validar os dados na criação de um novo agendamento. */

public record AgendamentoRequest(
        @NotNull(message = "O ID do usuário é obrigatório.")
        Long usuarioId,

        @NotNull(message = "O ID do barbeiro é obrigatório.")
        Long barbeiroId,

        @NotNull(message = "Pelo menos um ID de serviço deve ser informado.")
        Long servicoId,

        @NotNull(message = "A data e hora do agendamento devem ser informadas.")
        @Future(message = "A data do agendamento deve ser uma data futura.")
        LocalDateTime dataHora
) {
}
