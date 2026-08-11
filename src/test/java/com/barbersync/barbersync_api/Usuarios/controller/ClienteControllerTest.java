package com.barbersync.barbersync_api.Usuarios.controller;

import com.barbersync.barbersync_api.Usuarios.classes.Cliente;
import com.barbersync.barbersync_api.Usuarios.classes.Usuario;
import com.barbersync.barbersync_api.Usuarios.dtos.*;
import com.barbersync.barbersync_api.Usuarios.repository.ClienteRepository;
import com.barbersync.barbersync_api.Usuarios.services.ClienteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteService clienteService;

    @MockitoBean
    private ClienteRepository clienteRepository;

    @Autowired
    private JacksonTester<DadosCadastroCliente> cadastroClienteJacksonTester;

    @Autowired
    private JacksonTester<DadosAlteracaoCliente> alteracaoClienteJacksonTester;

    @Test
    @DisplayName("Deve retornar status 201 (CREATED) ao cadastrar um cliente com dados válidos")
    @WithMockUser
    void registrarCliente_cenario1() throws Exception {
        // Given
        DadosCadastroCliente dados = new DadosCadastroCliente(
                "Maria Cliente",
                "maria@cliente.com",
                "senha123",
                "11999998888"
        );

        var usuarioTeste = new Usuario(1L, dados.nome(), dados.email(), dados.senha(), Roles.CLIENTE);
        var clienteTeste = new Cliente(1L, dados.telefone(), usuarioTeste);

        var jsonBody = cadastroClienteJacksonTester.write(dados).getJson();

        when(clienteService.cadastrarUsuarioCliente(any())).thenReturn(clienteTeste);

        // When/Then
        mockMvc.perform(post("/clientes")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Deve retornar status 400 (BAD_REQUEST) ao cadastrar cliente com email inválido")
    @WithMockUser
    void registrarCliente_cenario2() throws Exception {
        // Given
        DadosCadastroCliente dados = new DadosCadastroCliente(
                "Maria Cliente",
                "emailinvalido",
                "senha123",
                "11999998888"
        );

        var jsonBody = cadastroClienteJacksonTester.write(dados).getJson();

        // When/Then
        mockMvc.perform(post("/clientes")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar status 400 (BAD_REQUEST) ao cadastrar cliente com telefone em formato inválido")
    @WithMockUser
    void registrarCliente_cenario3() throws Exception {
        // Given
        DadosCadastroCliente dados = new DadosCadastroCliente(
                "Maria Cliente",
                "maria@cliente.com",
                "senha123",
                "1199999"
        );

        var jsonBody = cadastroClienteJacksonTester.write(dados).getJson();

        // When/Then
        mockMvc.perform(post("/clientes")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar status 200 (OK) ao alterar um cliente com dados válidos")
    @WithMockUser
    void alterarCliente_cenario1() throws Exception {
        // Given
        DadosAlteracaoCliente dados = new DadosAlteracaoCliente(
                1L,
                "Maria Cliente Atualizada",
                "maria.nova@cliente.com",
                null,
                "11999998888"
        );

        var usuarioTeste = new Usuario(1L, dados.nome(), dados.email(), "senha123", Roles.CLIENTE);
        var clienteTeste = new Cliente(1L, dados.telefone(), usuarioTeste);

        var jsonBody = alteracaoClienteJacksonTester.write(dados).getJson();

        when(clienteService.alterarUsuarioCliente(any())).thenReturn(clienteTeste);

        // When/Then
        mockMvc.perform(put("/clientes")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar status 400 (BAD_REQUEST) ao alterar cliente com email inválido")
    @WithMockUser
    void alterarCliente_cenario2() throws Exception {
        // Given
        DadosAlteracaoCliente dados = new DadosAlteracaoCliente(
                1L,
                "Maria Cliente",
                "emailinvalido",
                null,
                "11999998888"
        );

        var jsonBody = alteracaoClienteJacksonTester.write(dados).getJson();

        // When/Then
        mockMvc.perform(put("/clientes")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar status 204 (NO_CONTENT) ao deletar um cliente existente")
    @WithMockUser
    void deletarCliente_cenario1() throws Exception {
        // Given
        Long id = 1L;

        // When/Then
        mockMvc.perform(delete("/clientes/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar status 200 (OK) ao listar clientes com sucesso")
    @WithMockUser
    void listarClientes_cenario1() throws Exception {
        // Given
        var usuarioTeste = new Usuario(1L, "Maria Cliente", "maria@cliente.com", "senha123", Roles.CLIENTE);
        var clienteTeste = new Cliente(1L, "11999998888", usuarioTeste);

        List<Cliente> clientes = List.of(clienteTeste);
        Page<Cliente> page = new PageImpl<>(clientes, PageRequest.of(0, 10), 1);

        when(clienteRepository.findAll(any(Pageable.class))).thenReturn(page);

        // When/Then
        mockMvc.perform(get("/clientes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
