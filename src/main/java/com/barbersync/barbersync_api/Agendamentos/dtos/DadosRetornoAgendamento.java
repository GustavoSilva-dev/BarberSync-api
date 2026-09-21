package com.barbersync.barbersync_api.Agendamentos.dtos;

import com.barbersync.barbersync_api.Agendamentos.classes.Agendamento;
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
    public DadosRetornoAgendamento(Agendamento agendamento) {
        this(
                agendamento.getDataHoraInicio(),
                agendamento.getDataHoraFinal(),
                new DadosRetornoCliente(
                        agendamento.getCliente().getUsuario().getNome(),
                        agendamento.getCliente().getUsuario().getEmail(),
                        agendamento.getCliente().getTelefone(),
                        agendamento.getCliente().getUsuario().getRole()
                ),
                new DadosRetornoBarbeiro(
                        agendamento.getBarbeiro().getUsuario().getNome(),
                        agendamento.getBarbeiro().getUsuario().getEmail(),
                        agendamento.getBarbeiro().getTelefone(),
                        agendamento.getBarbeiro().getCpf(),
                        agendamento.getBarbeiro().getUsuario().getRole()
                ),
                new DadosRetornoServico(
                        agendamento.getServico().getNome(),
                        agendamento.getServico().getDescricao(),
                        agendamento.getServico().getPreco(),
                        agendamento.getServico().getDuracaoEmMinutos(),
                        agendamento.getServico().getAtivo()
                )
        );
    }
}
