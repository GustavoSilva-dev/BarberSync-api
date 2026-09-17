package com.barbersync.barbersync_api.Agendamentos.components;

import com.barbersync.barbersync_api.Agendamentos.dtos.DadosCadastroAgendamento;
import com.barbersync.barbersync_api.Agendamentos.repository.AgendamentoRepository;
import com.barbersync.barbersync_api.Servicos.repository.ServicoRepository;
import com.barbersync.barbersync_api.Usuarios.repository.BarbeiroRepository;
import com.barbersync.barbersync_api.infra.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ValidadorHorarioBarbeiro implements ValidadorAgendamento {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private BarbeiroRepository barbeiroRepository;

    @Override
    public void validarAgendamento(DadosCadastroAgendamento dados) throws ValidacaoException {
        var servico = servicoRepository.getReferenceById(dados.servicoId());
        LocalDateTime dataFinal = dados.dataHoraInicio().plusMinutes(servico.getDuracaoEmMinutos());

        try {
            Boolean validar = agendamentoRepository.findByAgendamentoBarbeiroConflict(dados.barbeiroId(), dados.dataHoraInicio(), dataFinal);

            if(validar) throw new ValidacaoException("Conflito de horários identificado, agende em outro momento!");
        } catch (ValidacaoException e){
            throw new ValidacaoException(e.getMessage());
        }

    }
}
