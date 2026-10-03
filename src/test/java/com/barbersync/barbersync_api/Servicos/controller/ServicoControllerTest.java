package com.barbersync.barbersync_api.Servicos.controller;

import com.barbersync.barbersync_api.Servicos.classes.Servico;
import com.barbersync.barbersync_api.Servicos.dtos.DadosAlterarServico;
import com.barbersync.barbersync_api.Servicos.dtos.DadosCadastroServico;
import com.barbersync.barbersync_api.Servicos.dtos.DadosRetornoServico;
import com.barbersync.barbersync_api.Servicos.repository.ServicoRepository;
import com.barbersync.barbersync_api.Servicos.services.ServicoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureJsonTesters
@AutoConfigureMockMvc
class ServicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ServicoService service;

    @MockitoBean
    private ServicoRepository repository;

    @Autowired
    private JacksonTester<DadosCadastroServico> cadastroServicoJacksonTester;

    @Autowired
    private JacksonTester<DadosAlterarServico> alterarServicoJacksonTester;

    @Test
    @DisplayName("Retornar status 201 ao criar servico com sucesso")
    @WithMockUser(authorities = "ADMIN")
    void simularCenario01() throws Exception {
        DadosCadastroServico dados = new DadosCadastroServico("Degrade", "Degrade fade-in nas laterais do cabelo", BigDecimal.valueOf(25.0), 60);

        var servico = new Servico(1L, dados.nome(), dados.descricao(), dados.preco(), dados.duracaoEmMinutos(), true);

        var jsonbody = cadastroServicoJacksonTester.write(dados).getJson();

        when(service.cadastrarServico(any())).thenReturn(servico);
        when(repository.save(any())).thenReturn(servico);

        mockMvc.perform(post("/servico")
                .content(jsonbody)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Retornar status 200 ao alterar servico com sucesso")
    @WithMockUser(authorities = "ADMIN")
    void simularCenario02() throws Exception {
        DadosAlterarServico dados = new DadosAlterarServico("Degrade Novo", "Nova descrição", BigDecimal.valueOf(30.0), 45);
        var servico = new Servico(1L, dados.nome(), dados.descricao(), dados.preco(), dados.duracaoEmMinutos(), true);

        var jsonbody = alterarServicoJacksonTester.write(dados).getJson();

        when(service.alterarServico(eq(1L), any())).thenReturn(servico);

        mockMvc.perform(put("/servico/1")
                .content(jsonbody)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Retornar status 204 ao excluir servico com sucesso")
    @WithMockUser(authorities = "ADMIN")
    void simularCenario03() throws Exception {
        mockMvc.perform(delete("/servico/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Retornar status 200 ao listar servicos com sucesso")
    @WithMockUser(authorities = "ADMIN")
    void simularCenario04() throws Exception {
        var servico = new Servico(1L, "Degrade", "Desc", BigDecimal.valueOf(25.0), 60, true);
        var page = new PageImpl<>(List.of(servico), PageRequest.of(0, 10), 1);

        when(service.listarServicosAtivos(any())).thenReturn(page);

        mockMvc.perform(get("/servico"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Retornar status 400 ao cadastrar servico com preço invalido")
    @WithMockUser(authorities = "ADMIN")
    void simularFalha01() throws Exception {
        DadosCadastroServico dados = new DadosCadastroServico("Nome", "Desc", BigDecimal.valueOf(0), 60);

        var jsonbody = cadastroServicoJacksonTester.write(dados).getJson();

        mockMvc.perform(post("/servico")
                .content(jsonbody)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }
}