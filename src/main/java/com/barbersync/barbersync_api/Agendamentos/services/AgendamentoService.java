package com.barbersync.barbersync_api.Agendamentos.services;

import com.barbersync.barbersync_api.Agendamentos.classes.Agendamento;
import com.barbersync.barbersync_api.Agendamentos.components.ValidadorAgendamento;
import com.barbersync.barbersync_api.Agendamentos.dtos.DadosCadastroAgendamento;
import com.barbersync.barbersync_api.Agendamentos.repository.AgendamentoRepository;
import com.barbersync.barbersync_api.Servicos.repository.ServicoRepository;
import com.barbersync.barbersync_api.Usuarios.repository.BarbeiroRepository;
import com.barbersync.barbersync_api.Usuarios.repository.ClienteRepository;
import com.barbersync.barbersync_api.infra.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class AgendamentoService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private BarbeiroRepository barbeiroRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    private List<ValidadorAgendamento> validadores;

    public Agendamento validarDadosAgendamento(DadosCadastroAgendamento dados){
        var horario = dados.dataHoraInicio();

        if(!clienteRepository.existsById(dados.clienteId())){
            throw new ValidacaoException("ID do Cliente não existe.");
        }

        if(!barbeiroRepository.existsById(dados.barbeiroId())){
            throw new ValidacaoException("ID do Barbeiro não existe.");
        }

        if(!servicoRepository.existsById(dados.servicoId())){
            throw new ValidacaoException("ID do Serviço não existe.");
        }

        validadores.stream().forEach(validador -> validador.validarAgendamento(dados));

        var barbeiro = barbeiroRepository.getReferenceById(dados.barbeiroId());
        var cliente = clienteRepository.getReferenceById(dados.clienteId());
        var servico = servicoRepository.getReferenceById(dados.servicoId());

        LocalDateTime finalTime = dados.dataHoraInicio().plusMinutes(servico.getDuracaoEmMinutos());

        Agendamento agendamento = new Agendamento(null, dados.dataHoraInicio(), finalTime, dados.statusAgendamento(), barbeiro, cliente, servico);

        return agendamento;
    }
}
