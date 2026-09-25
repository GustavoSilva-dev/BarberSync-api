package com.barbersync.barbersync_api.Agendamentos.services;

import com.barbersync.barbersync_api.Agendamentos.classes.Agendamento;
import com.barbersync.barbersync_api.Agendamentos.components.ValidadorAgendamento;
import com.barbersync.barbersync_api.Agendamentos.dtos.DadosCadastroAgendamento;
import com.barbersync.barbersync_api.Agendamentos.dtos.StatusAgendamento;
import com.barbersync.barbersync_api.Agendamentos.repository.AgendamentoRepository;
import com.barbersync.barbersync_api.Email.dtos.DadosEmail;
import com.barbersync.barbersync_api.Email.services.EmailService;
import com.barbersync.barbersync_api.Servicos.repository.ServicoRepository;
import com.barbersync.barbersync_api.Usuarios.classes.Cliente;
import com.barbersync.barbersync_api.Usuarios.classes.Usuario;
import com.barbersync.barbersync_api.Usuarios.repository.BarbeiroRepository;
import com.barbersync.barbersync_api.Usuarios.repository.ClienteRepository;
import com.barbersync.barbersync_api.infra.exception.ValidacaoException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Autowired
    private List<ValidadorAgendamento> validadores;

    @Autowired
    private EmailService emailService;

    @Transactional
    public Agendamento validarDadosAgendamento(DadosCadastroAgendamento dados){
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        validadores.stream().forEach(validador -> validador.validarAgendamento(dados));

        var barbeiro = barbeiroRepository.getReferenceById(dados.barbeiroId());
        var cliente = clienteRepository.getReferenceById(dados.clienteId());
        var servico = servicoRepository.getReferenceById(dados.servicoId());

        LocalDateTime finalTime = dados.dataHoraInicio().plusMinutes(servico.getDuracaoEmMinutos());

        Agendamento agendamento = new Agendamento(null, dados.dataHoraInicio(), finalTime, StatusAgendamento.AGENDADO, barbeiro, cliente, servico);

        agendamentoRepository.save(agendamento);

        DadosEmail emailSucesso = new DadosEmail(
                agendamento.getCliente().getUsuario().getEmail(),
                "Agendamento realizado com sucesso!",
                "Olá, " + agendamento.getCliente().getUsuario().getNome().toUpperCase() + "!\n Seu agendamento com o barbeiro " + agendamento.getBarbeiro().getUsuario().getNome().toUpperCase() + " foi confirmado! Segue abaixo as informações detalhadas: \n\nData e hora de início: " + formatador.format(agendamento.getDataHoraInicio()) + "\nData e hora estimada de finalização: " + formatador.format(agendamento.getDataHoraFinal()) + "\n\nLhe aguardamos até lá! \n\nAtenciosamente,\nBarberSync System."
        );
        emailService.sendEmail(emailSucesso);

        return agendamento;
    }

    @Transactional
    public Agendamento validarAgendamentoConcluido(Long id) {
        var agendamentoExistente = agendamentoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado"));

        if(agendamentoExistente.getStatusAgendamento() != StatusAgendamento.AGENDADO) throw new ValidacaoException("Agendamento inválido para conclusão - Status inválido para alteração.");

        agendamentoExistente.setStatusAgendamento(StatusAgendamento.CONCLUIDO);

        agendamentoRepository.save(agendamentoExistente);

        return agendamentoExistente;
    }

@Transactional
public Agendamento cancelarAgendamento(Long id, Usuario usuarioAutenticado) {
    var agendamento = agendamentoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado"));

    if (agendamento.getStatusAgendamento() == StatusAgendamento.CONCLUIDO) {
        throw new ValidacaoException("Agendamento não pode ser cancelado pois já está concluído.");
    }

    if(usuarioAutenticado.isCliente()){
        Cliente clienteAutenticado = clienteRepository.findByUsuarioEmail(usuarioAutenticado.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Perfil de cliente não encontrado."));

        if (!agendamento.getCliente().getId().equals(clienteAutenticado.getId())) {
            throw new ValidacaoException("ACESSO NEGADO: Você não pode cancelar o agendamento de outra pessoa.");
        }

        if (agendamento.getDataHoraInicio().isBefore(LocalDateTime.now().plusMinutes(30))) {
            throw new ValidacaoException("PRAZO DE CANCELAMENTO EXPIRADO: Apenas agendamentos com mais de 30 minutos de antecedência podem ser cancelados. Contate a barbearia diretamente.");
        }
    }

    agendamento.setStatusAgendamento(StatusAgendamento.CANCELADO);
    agendamentoRepository.save(agendamento);

    return agendamento;
    }

    @Transactional
    public void excluirAgendamento(Long id) {
        var agendamento = agendamentoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado"));

        if (agendamento.getStatusAgendamento() == StatusAgendamento.CONCLUIDO) {
            throw new ValidacaoException("Agendamento concluído não pode ser excluído.");
        }

        agendamentoRepository.delete(agendamento);
    }
}
