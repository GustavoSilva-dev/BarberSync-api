package com.barbersync.barbersync_api.Usuarios.controller;

import com.barbersync.barbersync_api.Usuarios.classes.Admin;
import com.barbersync.barbersync_api.Usuarios.classes.Barbeiro;
import com.barbersync.barbersync_api.Usuarios.classes.Usuario;
import com.barbersync.barbersync_api.Usuarios.dtos.*;
import com.barbersync.barbersync_api.Usuarios.repository.AdminRepository;
import com.barbersync.barbersync_api.Usuarios.repository.UsuariosRepository;
import com.barbersync.barbersync_api.Usuarios.services.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdminService adminService;

    @MockitoBean
    private AdminRepository adminRepository;

    @MockitoBean
    private UsuariosRepository usuariosRepository;

    @Autowired
    private JacksonTester<DadosCadastroAdmin> cadastroAdminJacksonTester;

    @Autowired
    private JacksonTester<DadosAlteracaoAdmin> alteracaoAdminJacksonTester;

    @Test
    @DisplayName("Deve retornar status 201 (CREATED) ao cadastrar um admin com dados válidos")
    @WithMockUser(roles = "ADMIN")
    void registrarAdmin_cenario1() throws Exception {
        // Given
        DadosCadastroAdmin dados = new DadosCadastroAdmin("Admin Teste", "admin@teste.com", "senha123", "12345");

        var usuarioTeste = new Usuario(1L, dados.nome(), dados.email(), dados.senha(), Roles.ADMIN);
        var adminTeste = new Admin(1L, usuarioTeste, dados.adminKey(), Status.ATIVO);

        var jsonBody = cadastroAdminJacksonTester.write(dados).getJson();

        when(adminService.cadastrarUsuarioAdmin(any())).thenReturn(adminTeste);

        // When/Then
        mockMvc.perform(post("/admins")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Deve retornar status 400 (BAD REQUEST) ao cadastrar um admin com email inválido")
    @WithMockUser(roles = "ADMIN")
    void registrarAdmin_cenario2() throws Exception {
        // Given
        DadosCadastroAdmin dados = new DadosCadastroAdmin(
                "Admin Teste",
                "admin",
                "senha123",
                "12345");

        var jsonBody = cadastroAdminJacksonTester.write(dados).getJson();

        // When/Then
        mockMvc.perform(post("/admins")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar status 200 (OK) ao alterar um admin com dados válidos")
    @WithMockUser(roles = "ADMIN")
    void alterarAdmin_cenario1() throws Exception {
        // Given
        DadosAlteracaoAdmin dados = new DadosAlteracaoAdmin(1L, "Admin Atualizado", "admin@teste.com", "novaSenha", "54321");

        String jsonBody = alteracaoAdminJacksonTester.write(dados).getJson();

        // When/Then
        mockMvc.perform(put("/admins")
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar status 204 (No Content) ao desativar um admin existente")
    @WithMockUser(roles = "ADMIN")
    void excluirAdmin_cenario1() throws Exception {
        // Given
        Long id = 1L;

        // When/Then
        mockMvc.perform(delete("/admins/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar status 200 (OKK) o listar os admins ativos")
    @WithMockUser(roles = "ADMIN")
    void listarAdmin_cenario1() throws Exception {
        var usuarioTeste = new Usuario(1L, "Jeff Admin", "jeff123", "jeff@admin.com", Roles.ADMIN);
        var adminTeste = new Admin(1L, usuarioTeste, "3334455", Status.ATIVO);

        List<Admin> adminList = List.of(adminTeste);
        Page<Admin> page = new PageImpl<>(adminList, PageRequest.of(0, 10), 1);

        when(adminRepository.findAllbyAtivo(any())).thenReturn(page);

        mockMvc.perform(get("/admins").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}