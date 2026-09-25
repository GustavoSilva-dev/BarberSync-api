package com.barbersync.barbersync_api.Agendamentos.controller;

import com.barbersync.barbersync_api.Agendamentos.classes.Agendamento;
import com.barbersync.barbersync_api.Agendamentos.dtos.AgendamentoClientSide;
import com.barbersync.barbersync_api.Agendamentos.dtos.DadosCadastroAgendamento;
import com.barbersync.barbersync_api.Agendamentos.dtos.DadosRetornoAgendamento;
import com.barbersync.barbersync_api.Agendamentos.dtos.StatusAgendamento;
import com.barbersync.barbersync_api.Agendamentos.repository.AgendamentoRepository;
import com.barbersync.barbersync_api.Agendamentos.services.AgendamentoService;
import com.barbersync.barbersync_api.Servicos.classes.Servico;
import com.barbersync.barbersync_api.Servicos.repository.ServicoRepository;
import com.barbersync.barbersync_api.Usuarios.classes.Barbeiro;
import com.barbersync.barbersync_api.Usuarios.classes.Cliente;
import com.barbersync.barbersync_api.Usuarios.classes.Usuario;
import com.barbersync.barbersync_api.Usuarios.dtos.Roles;
import org.springframework.http.MediaType;
import com.barbersync.barbersync_api.Usuarios.dtos.Status;
import com.barbersync.barbersync_api.Usuarios.repository.BarbeiroRepository;
import com.barbersync.barbersync_api.Usuarios.repository.ClienteRepository;
import com.barbersync.barbersync_api.infra.exception.ValidacaoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class AgendamentoControllerTest {

    @MockitoBean
    private AgendamentoRepository agendamentoRepository;

    @MockitoBean
    private ClienteRepository clienteRepository;

    @MockitoBean
    private BarbeiroRepository barbeiroRepository;

    @MockitoBean
    private ServicoRepository servicoRepository;

    @MockitoBean
    private AgendamentoService agendamentoService;

    @Autowired
    private JacksonTester<DadosCadastroAgendamento> dadosCadastroAgendamentoJacksonTester;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Deve retornar status 201 ao criar agendamento válido")
    @WithMockUser(authorities = "ADMIN")
    void criarAgendamentoStatus201() throws Exception {
        when(agendamentoService.validarDadosAgendamento(any())).thenReturn(agendamento(StatusAgendamento.AGENDADO));

        mockMvc.perform(post("/agendamentos/admin-side")
                .content(jsonCadastroAgendamento())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Deve retornar status 200 ao concluir um agendamento")
    @WithMockUser(authorities = "ADMIN")
    void concluirAgendamentoStatus200() throws Exception {
        when(agendamentoService.validarAgendamentoConcluido(1L))
                .thenReturn(agendamento(StatusAgendamento.CONCLUIDO));

        mockMvc.perform(patch("/agendamentos/concluir/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar status 200 ao cancelar um agendamento")
    @WithMockUser(authorities = "ADMIN")
    void cancelarAgendamentoStatus200() throws Exception {
        when(agendamentoService.cancelarAgendamento(eq(1L), isNull()))
                .thenReturn(agendamento(StatusAgendamento.CANCELADO));

        mockMvc.perform(patch("/agendamentos/cancelar/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar status 204 ao excluir um agendamento")
    @WithMockUser(authorities = "ADMIN")
    void excluirAgendamentoStatus204() throws Exception {
        mockMvc.perform(delete("/agendamentos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar status 400 ao dar erro de validação genérica")
    @WithMockUser(authorities = "ADMIN")
    void criarAgendamentoComBarbeiroInativoStatus400() throws Exception {
        when(agendamentoService.validarDadosAgendamento(any()))
                .thenThrow(new ValidacaoException("Barbeiro não encontrado ou inativo no sistema."));

        mockMvc.perform(post("/agendamentos/admin-side")
                        .content(jsonCadastroAgendamento())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private String jsonCadastroAgendamento() throws Exception {
        var dados = new DadosCadastroAgendamento(
                LocalDateTime.parse("30/09/2026 13:00", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                1L,
                1L,
                1L
        );

        return dadosCadastroAgendamentoJacksonTester.write(dados).getJson();
    }

    private Agendamento agendamento(StatusAgendamento status) {
        var usuario = new Usuario(1L, "Gerson", "senhateste", "email@gmail.com", Roles.CLIENTE);
        var cliente = new Cliente(1L, "11999992222", Status.ATIVO, usuario);
        var barbeiro = new Barbeiro(1L, usuario, "55599900011", "11999992222", Status.ATIVO);
        var servico = new Servico(1L, "Corte de Barba", "Corte de qualquer tipo diverso de barba",
                BigDecimal.valueOf(34.99), 60, true);

        return new Agendamento(
                1L,
                LocalDateTime.of(2026, 9, 30, 13, 0),
                LocalDateTime.of(2026, 9, 30, 14, 0),
                status,
                barbeiro,
                cliente,
                servico
        );
    }
}