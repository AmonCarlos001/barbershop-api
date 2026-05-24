package com.ribeiro.barbershop.dto.response;

import java.time.LocalDateTime;

/** Record estruturado para expor os dados consolidados de um agendamento na API. */

public record AgendamentoResponse(
       Long id,
       String clienteNome,
       String barbeiroNome,
       String servicoNome,
       Double preco,
       LocalDateTime dataHora,
       String status
) {
}
