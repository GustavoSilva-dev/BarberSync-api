package com.barbersync.barbersync_api.Agendamentos.services;

import com.barbersync.barbersync_api.Agendamentos.classes.Agendamento;
import com.barbersync.barbersync_api.Agendamentos.components.ValidadorAgendamento;
import com.barbersync.barbersync_api.Agendamentos.dtos.DadosCadastroAgendamento;
import com.barbersync.barbersync_api.Agendamentos.dtos.StatusAgendamento;
import com.barbersync.barbersync_api.Agendamentos.repository.AgendamentoRepository;
import com.barbersync.barbersync_api.Servicos.classes.Servico;
import com.barbersync.barbersync_api.Servicos.repository.ServicoRepository;
import com.barbersync.barbersync_api.Usuarios.classes.Barbeiro;
import com.barbersync.barbersync_api.Usuarios.classes.Cliente;
import com.barbersync.barbersync_api.Usuarios.classes.Usuario;
import com.barbersync.barbersync_api.Usuarios.dtos.Roles;
import com.barbersync.barbersync_api.Usuarios.dtos.Status;
import com.barbersync.barbersync_api.Usuarios.repository.BarbeiroRepository;
import com.barbersync.barbersync_api.Usuarios.repository.ClienteRepository;
import com.barbersync.barbersync_api.infra.exception.ValidacaoException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendamentoServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private BarbeiroRepository barbeiroRepository;

    @Mock
    private ServicoRepository servicoRepository;

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @Mock
    private List<ValidadorAgendamento> validadores;

    @InjectMocks
    private AgendamentoService service;

    @Test
    @DisplayName("Deve validar e salvar um agendamento com o horário final calculado")
    void validarDadosAgendamentoDeveSalvarAgendamentoValido() {
        var dados = dadosCadastro(LocalDateTime.of(2026, 9, 30, 13, 0));
        var barbeiro = barbeiro();
        var cliente = cliente();
        var servico = servico(60);

        when(validadores.stream()).thenReturn(List.<ValidadorAgendamento>of().stream());
        when(barbeiroRepository.getReferenceById(1L)).thenReturn(barbeiro);
        when(clienteRepository.getReferenceById(1L)).thenReturn(cliente);
        when(servicoRepository.getReferenceById(1L)).thenReturn(servico);
        when(agendamentoRepository.save(any(Agendamento.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var resultado = service.validarDadosAgendamento(dados);

        assertEquals(dados.dataHoraInicio(), resultado.getDataHoraInicio());
        assertEquals(LocalDateTime.of(2026, 9, 30, 14, 0), resultado.getDataHoraFinal());
        assertEquals(StatusAgendamento.AGENDADO, resultado.getStatusAgendamento());
        assertSame(barbeiro, resultado.getBarbeiro());
        assertSame(cliente, resultado.getCliente());
        assertSame(servico, resultado.getServico());
        verify(agendamentoRepository).save(resultado);
    }

    @Test
    @DisplayName("Deve interromper a criação quando um validador rejeitar os dados")
    void validarDadosAgendamentoDevePropagarFalhaDoValidador() {
        var dados = dadosCadastro(LocalDateTime.of(2026, 9, 30, 13, 0));
        var erro = new ValidacaoException("Conflito de horários identificado");
        var validador = org.mockito.Mockito.mock(ValidadorAgendamento.class);

        when(validadores.stream()).thenReturn(List.of(validador).stream());
        doThrow(erro).when(validador).validarAgendamento(dados);

        var excecao = assertThrows(ValidacaoException.class,
                () -> service.validarDadosAgendamento(dados));

        assertSame(erro, excecao);
        verify(validador).validarAgendamento(dados);
        verifyNoInteractions(barbeiroRepository, clienteRepository, servicoRepository, agendamentoRepository);
    }

    @Test
    @DisplayName("Deve concluir um agendamento que está agendado")
    void validarAgendamentoConcluidoDeveAlterarStatus() {
        var agendamento = agendamento(StatusAgendamento.AGENDADO);
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
        when(agendamentoRepository.save(agendamento)).thenReturn(agendamento);

        var resultado = service.validarAgendamentoConcluido(1L);

        assertEquals(StatusAgendamento.CONCLUIDO, resultado.getStatusAgendamento());
        verify(agendamentoRepository).save(agendamento);
    }

    @Test
    @DisplayName("Deve rejeitar conclusão de agendamento inexistente")
    void validarAgendamentoConcluidoDeveFalharQuandoNaoEncontrado() {
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.validarAgendamentoConcluido(1L));

        verify(agendamentoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve rejeitar conclusão de agendamento que não está agendado")
    void validarAgendamentoConcluidoDeveFalharComStatusInvalido() {
        var agendamento = agendamento(StatusAgendamento.CANCELADO);
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));

        var excecao = assertThrows(ValidacaoException.class,
                () -> service.validarAgendamentoConcluido(1L));

        assertEquals("Agendamento inválido para conclusão - Status inválido para alteração.", excecao.getMessage());
        verify(agendamentoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve cancelar agendamento de administrador")
    void cancelarAgendamentoDevePermitirAdministrador() {
        var agendamento = agendamento(StatusAgendamento.AGENDADO);
        var administrador = usuario(Roles.ADMIN);
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));

        var resultado = service.cancelarAgendamento(1L, administrador);

        assertSame(agendamento, resultado);
        assertEquals(StatusAgendamento.CANCELADO, agendamento.getStatusAgendamento());
        verify(agendamentoRepository).save(agendamento);
        verifyNoInteractions(clienteRepository);
    }

    @Test
    @DisplayName("Deve cancelar agendamento do próprio cliente com antecedência")
    void cancelarAgendamentoDevePermitirClienteAssociado() {
        var agendamento = agendamento(StatusAgendamento.AGENDADO);
        agendamento.setDataHoraInicio(LocalDateTime.now().plusHours(2));
        var cliente = cliente();
        var usuarioCliente = cliente.getUsuario();

        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
        when(clienteRepository.findByUsuarioEmail(usuarioCliente.getEmail())).thenReturn(Optional.of(cliente));

        var resultado = service.cancelarAgendamento(1L, usuarioCliente);

        assertSame(agendamento, resultado);
        assertEquals(StatusAgendamento.CANCELADO, agendamento.getStatusAgendamento());
        verify(agendamentoRepository).save(agendamento);
    }

    @Test
    @DisplayName("Deve rejeitar cancelamento de agendamento inexistente")
    void cancelarAgendamentoDeveFalharQuandoNaoEncontrado() {
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.cancelarAgendamento(1L, usuario(Roles.ADMIN)));

        verify(agendamentoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve rejeitar cancelamento de agendamento concluído")
    void cancelarAgendamentoDeveFalharQuandoConcluido() {
        var agendamento = agendamento(StatusAgendamento.CONCLUIDO);
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));

        var excecao = assertThrows(ValidacaoException.class,
                () -> service.cancelarAgendamento(1L, usuario(Roles.ADMIN)));

        assertEquals("Agendamento não pode ser cancelado pois já está concluído.", excecao.getMessage());
        verify(agendamentoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve rejeitar cancelamento quando o perfil do cliente não existe")
    void cancelarAgendamentoDeveFalharQuandoPerfilNaoEncontrado() {
        var agendamento = agendamento(StatusAgendamento.AGENDADO);
        var usuarioCliente = usuario(Roles.CLIENTE);
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
        when(clienteRepository.findByUsuarioEmail(usuarioCliente.getEmail())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> service.cancelarAgendamento(1L, usuarioCliente));

        verify(agendamentoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve rejeitar cancelamento de agendamento de outro cliente")
    void cancelarAgendamentoDeveFalharQuandoClienteNaoAssociado() {
        var agendamento = agendamento(StatusAgendamento.AGENDADO);
        var outroCliente = new Cliente(2L, "11988887777", Status.ATIVO, usuario(Roles.CLIENTE));
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
        when(clienteRepository.findByUsuarioEmail(outroCliente.getUsuario().getEmail()))
                .thenReturn(Optional.of(outroCliente));

        var excecao = assertThrows(ValidacaoException.class,
                () -> service.cancelarAgendamento(1L, outroCliente.getUsuario()));

        assertEquals("ACESSO NEGADO: Você não pode cancelar o agendamento de outra pessoa.", excecao.getMessage());
        verify(agendamentoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve rejeitar cancelamento de cliente sem antecedência mínima")
    void cancelarAgendamentoDeveFalharSemAntecedencia() {
        var agendamento = agendamento(StatusAgendamento.AGENDADO);
        agendamento.setDataHoraInicio(LocalDateTime.now().plusMinutes(10));
        var cliente = cliente();
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
        when(clienteRepository.findByUsuarioEmail(cliente.getUsuario().getEmail())).thenReturn(Optional.of(cliente));

        var excecao = assertThrows(ValidacaoException.class,
                () -> service.cancelarAgendamento(1L, cliente.getUsuario()));

        assertEquals("PRAZO DE CANCELAMENTO EXPIRADO: Apenas agendamentos com mais de 30 minutos de antecedência podem ser cancelados. Contate a barbearia diretamente.", excecao.getMessage());
        verify(agendamentoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve excluir agendamento não concluído")
    void excluirAgendamentoDeveExcluirAgendamentoValido() {
        var agendamento = agendamento(StatusAgendamento.CANCELADO);
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));

        service.excluirAgendamento(1L);

        verify(agendamentoRepository).delete(agendamento);
    }

    @Test
    @DisplayName("Deve rejeitar exclusão de agendamento inexistente")
    void excluirAgendamentoDeveFalharQuandoNaoEncontrado() {
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.excluirAgendamento(1L));

        verify(agendamentoRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Deve rejeitar exclusão de agendamento concluído")
    void excluirAgendamentoDeveFalharQuandoConcluido() {
        var agendamento = agendamento(StatusAgendamento.CONCLUIDO);
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));

        var excecao = assertThrows(ValidacaoException.class,
                () -> service.excluirAgendamento(1L));

        assertEquals("Agendamento concluído não pode ser excluído.", excecao.getMessage());
        verify(agendamentoRepository, never()).delete(any());
    }

    private DadosCadastroAgendamento dadosCadastro(LocalDateTime dataHoraInicio) {
        return new DadosCadastroAgendamento(dataHoraInicio, 1L, 1L, 1L);
    }

    private Agendamento agendamento(StatusAgendamento status) {
        return new Agendamento(
                1L,
                LocalDateTime.now().plusHours(2),
                LocalDateTime.now().plusHours(3),
                status,
                barbeiro(),
                cliente(),
                servico(60)
        );
    }

    private Cliente cliente() {
        return new Cliente(1L, "11999992222", Status.ATIVO, usuario(Roles.CLIENTE));
    }

    private Barbeiro barbeiro() {
        return new Barbeiro(1L, usuario(Roles.BARBEIRO), "55599900011", "11999992222", Status.ATIVO);
    }

    private Servico servico(int duracaoEmMinutos) {
        return new Servico(1L, "Corte de Barba", "Corte de qualquer tipo diverso de barba",
                BigDecimal.valueOf(34.99), duracaoEmMinutos, true);
    }

    private Usuario usuario(Roles role) {
        return new Usuario(1L, "Gerson", "senhateste", "email-" + role.name().toLowerCase() + "@gmail.com", role);
    }
}
