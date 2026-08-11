package com.barbersync.barbersync_api.Usuarios.controller;

import com.barbersync.barbersync_api.Usuarios.classes.Barbeiro;
import com.barbersync.barbersync_api.Usuarios.classes.Usuario;
import com.barbersync.barbersync_api.Usuarios.dtos.*;
import com.barbersync.barbersync_api.Usuarios.repository.BarbeiroRepository;
import com.barbersync.barbersync_api.Usuarios.services.BarbeiroService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
class BarbeiroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BarbeiroService barbeiroService;

    @MockitoBean
    private BarbeiroRepository barbeiroRepository;

    @Autowired
    private JacksonTester<DadosCadastroBarbeiro> cadastroBarbeiroJacksonTester;

    @Autowired
    private JacksonTester<DadosAlteracaoBarbeiro> alteracaoBarbeiroJacksonTester;

    @Test
    @DisplayName("Deve retornar status 201 (CREATED) ao cadastrar um barbeiro com dados válidos")
    @WithMockUser
    void registrarBarbeiro_cenario1() throws Exception {
        // Given
        DadosCadastroBarbeiro dados = new DadosCadastroBarbeiro(
                "João Barbeiro",
                "joao@barbershop.com",
                "senha123",
                "11987654321",
                "12345678901"
        );

        var usuarioTeste = new Usuario(1L, dados.nome(), dados.email(), dados.senha(), Roles.BARBEIRO);
        var barbeirTeste = new Barbeiro(1L, usuarioTeste, dados.cpf(), dados.telefone(), Status.ATIVO);

        var jsonBody = cadastroBarbeiroJacksonTester.write(dados).getJson();

        when(barbeiroService.cadastrarUsuarioBarbeiro(any())).thenReturn(barbeirTeste);

        // When/Then
        mockMvc.perform(post("/barbeiros")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Deve retornar status 400 (BAD_REQUEST) ao cadastrar barbeiro com email inválido")
    @WithMockUser
    void registrarBarbeiro_cenario2() throws Exception {
        // Given
        DadosCadastroBarbeiro dados = new DadosCadastroBarbeiro(
                "João Barbeiro",
                "emailinvalido",
                "senha123",
                "11987654321",
                "12345678901"
        );

        var jsonBody = cadastroBarbeiroJacksonTester.write(dados).getJson();

        // When/Then
        mockMvc.perform(post("/barbeiros")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar status 400 (BAD_REQUEST) ao cadastrar barbeiro com telefone em formato inválido")
    @WithMockUser
    void registrarBarbeiro_cenario3() throws Exception {
        // Given
        DadosCadastroBarbeiro dados = new DadosCadastroBarbeiro(
                "João Barbeiro",
                "joao@barbershop.com",
                "senha123",
                "119876543",
                "12345678901"
        );

        var jsonBody = cadastroBarbeiroJacksonTester.write(dados).getJson();

        // When/Then
        mockMvc.perform(post("/barbeiros")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar status 400 (BAD_REQUEST) ao cadastrar barbeiro com CPF em formato inválido")
    @WithMockUser
    void registrarBarbeiro_cenario4() throws Exception {
        // Given
        DadosCadastroBarbeiro dados = new DadosCadastroBarbeiro(
                "João Barbeiro",
                "joao@barbershop.com",
                "senha123",
                "11987654321",
                "123456789"
        );

        var jsonBody = cadastroBarbeiroJacksonTester.write(dados).getJson();

        // When/Then
        mockMvc.perform(post("/barbeiros")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar status 200 (OK) ao alterar um barbeiro com dados válidos")
    @WithMockUser
    void alterarBarbeiro_cenario1() throws Exception {
        // Given
        DadosAlteracaoBarbeiro dados = new DadosAlteracaoBarbeiro(
                1L,
                "João Barbeiro Atualizado",
                "joao.novo@barbershop.com",
                null,
                "12345678901",
                "11987654321"
        );

        var usuarioTeste = new Usuario(1L, dados.nome(), dados.email(), "senha123", Roles.BARBEIRO);
        var barbeirTeste = new Barbeiro(1L, usuarioTeste, dados.cpf(), dados.telefone(), Status.ATIVO);

        var jsonBody = alteracaoBarbeiroJacksonTester.write(dados).getJson();

        when(barbeiroService.mudarBarbeiro(any())).thenReturn(barbeirTeste);

        // When/Then
        mockMvc.perform(put("/barbeiros")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar status 400 (BAD_REQUEST) ao alterar barbeiro com email inválido")
    @WithMockUser
    void alterarBarbeiro_cenario2() throws Exception {
        // Given
        DadosAlteracaoBarbeiro dados = new DadosAlteracaoBarbeiro(
                1L,
                "João Barbeiro",
                "emailinvalido",
                null,
                "12345678901",
                "11987654321"
        );

        var jsonBody = alteracaoBarbeiroJacksonTester.write(dados).getJson();

        // When/Then
        mockMvc.perform(put("/barbeiros")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar status 400 (BAD_REQUEST) ao alterar barbeiro com telefone em formato inválido")
    @WithMockUser
    void alterarBarbeiro_cenario3() throws Exception {
        // Given
        DadosAlteracaoBarbeiro dados = new DadosAlteracaoBarbeiro(
                1L,
                "João Barbeiro",
                "joao@barbershop.com",
                null,
                "12345678901",
                "119876543"
        );

        var jsonBody = alteracaoBarbeiroJacksonTester.write(dados).getJson();

        // When/Then
        mockMvc.perform(put("/barbeiros")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar status 400 (BAD_REQUEST) ao alterar barbeiro com CPF em formato inválido")
    @WithMockUser
    void alterarBarbeiro_cenario4() throws Exception {
        // Given
        DadosAlteracaoBarbeiro dados = new DadosAlteracaoBarbeiro(
                1L,
                "João Barbeiro",
                "joao@barbershop.com",
                null,
                "123456789",
                "11987654321"
        );

        var jsonBody = alteracaoBarbeiroJacksonTester.write(dados).getJson();

        // When/Then
        mockMvc.perform(put("/barbeiros")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar status 204 (NO_CONTENT) ao desativar um barbeiro existente")
    @WithMockUser
    void excluirBarbeiro_cenario1() throws Exception {
        // Given
        Long id = 1L;

        // When/Then
        mockMvc.perform(delete("/barbeiros/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar status 200 (OK) ao listar barbeiros com sucesso")
    @WithMockUser
    void listarBarbeiros_cenario1() throws Exception {
        // Given
        var usuarioTeste = new Usuario(1L, "João Barbeiro", "joao@barbershop.com", "senha123", Roles.BARBEIRO);
        var barbeirTeste = new Barbeiro(1L, usuarioTeste, "12345678901", "11987654321", Status.ATIVO);

        List<Barbeiro> barbeiros = List.of(barbeirTeste);
        Page<Barbeiro> page = new PageImpl<>(barbeiros, PageRequest.of(0, 10), 1);

        when(barbeiroRepository.findAllByAtivo(any(Pageable.class))).thenReturn(page);

        // When/Then
        mockMvc.perform(get("/barbeiros")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
