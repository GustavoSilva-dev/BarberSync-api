package com.barbersync.barbersync_api.Agendamentos.components;

import com.barbersync.barbersync_api.Agendamentos.dtos.DadosCadastroAgendamento;
import com.barbersync.barbersync_api.Agendamentos.repository.AgendamentoRepository;
import com.barbersync.barbersync_api.Servicos.repository.ServicoRepository;
import com.barbersync.barbersync_api.infra.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ValidadorHorarioCliente implements ValidadorAgendamento {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Override
    public void validarAgendamento(DadosCadastroAgendamento dados) {
        var servico = servicoRepository.getReferenceById(dados.servicoId());

        LocalDateTime dataFinal = dados.dataHoraInicio().plusMinutes(servico.getDuracaoEmMinutos());

        var validador = agendamentoRepository.findByAgendamentoClienteConflict(dados.clienteId(), dados.dataHoraInicio(), dataFinal);

        if(validador) throw new ValidacaoException("Conflito de horários identificado: Cliente já possui agendamentos no mesmo horário. Agende em outro momento!");
    }
}
