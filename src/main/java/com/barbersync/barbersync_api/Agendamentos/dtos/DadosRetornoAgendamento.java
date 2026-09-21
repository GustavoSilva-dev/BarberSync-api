package com.barbersync.barbersync_api.Agendamentos.dtos;

import com.barbersync.barbersync_api.Servicos.dtos.DadosRetornoServico;
import com.barbersync.barbersync_api.Usuarios.dtos.DadosRetornoBarbeiro;
import com.barbersync.barbersync_api.Usuarios.dtos.DadosRetornoCliente;

import java.time.LocalDateTime;

public record DadosRetornoAgendamento(
        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFinal,
        DadosRetornoCliente clienteAssociado,
        DadosRetornoBarbeiro barbeiroAssociado,
        DadosRetornoServico servicoAssociado
) {
}
