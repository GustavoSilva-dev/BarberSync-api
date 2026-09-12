package com.barbersync.barbersync_api.Agendamentos.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DadosCadastroAgendamento(
        @NotNull(message = "Digite a data e hora de início deste agendamento")
        LocalDateTime dataHoraInicio,
        @NotNull(message = "Digite o status atual do agendamento")
        StatusAgendamento statusAgendamento,
        @NotNull(message = "Associe o barbeiro ao agendamento")
        Long barbeiroId,
        @NotNull(message = "Associe o cliente ao agendamento")
        Long clienteId,
        @NotNull(message = "Associe o serviço ao agendamento. Ex: Corte Social")
        Long servicoId
) {
}
