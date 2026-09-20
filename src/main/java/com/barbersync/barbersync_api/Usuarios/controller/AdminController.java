package com.barbersync.barbersync_api.Usuarios.controller;

import com.barbersync.barbersync_api.Usuarios.classes.Admin;
import com.barbersync.barbersync_api.Usuarios.classes.Cliente;
import com.barbersync.barbersync_api.Usuarios.dtos.*;
import com.barbersync.barbersync_api.Usuarios.repository.AdminRepository;
import com.barbersync.barbersync_api.Usuarios.services.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminRepository repository;

    @PostMapping
    @Transactional
    @Operation(
            summary = "Cadastro de um administrador do sistema (apenas outro ADMIN pode realizar)",
            description = "Endpoint POST para o cadastro de um administrador do sistema do BarberSync, com dados de um usuário (nome, email, senha), CPF e status de ativação."
    )
    @SecurityRequirement(name = "bearer-key", scopes = { "ADMIN" })
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity registrarAdmin(@RequestBody @Valid DadosCadastroAdmin dados, UriComponentsBuilder uriBuilder) throws Exception {
        var admin = adminService.cadastrarUsuarioAdmin(dados);
        var uri = uriBuilder.path("/admins/{id}").buildAndExpand(admin.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosRetornoAdmin(admin));
    }

    @PutMapping
    @Transactional
    @Operation(
            summary = "Edição de um administrador do sistema (apenas outro ADMIN pode realizar)",
            description = "Endpoint PUT para a edição de um administrador do sistema do BarberSync, com dados de alteração."
    )
    @SecurityRequirement(name = "bearer-key", scopes = { "ADMIN" })
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity alterarAdmin(@RequestBody @Valid DadosAlteracaoAdmin dados) throws Exception {
        adminService.alterarUsuarioAdmin(dados);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(
            summary = "Exclusão de um administrador do sistema (apenas outro ADMIN pode realizar)",
            description = "Endpoint DELETE para a exclusão (safe delete) de um administrador do sistema do BarberSync."
    )
    @SecurityRequirement(name = "bearer-key", scopes = { "ADMIN" })
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity excluirBarbeiro(@PathVariable Long id) throws Exception {
        adminService.desativarAdmin(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(
            summary = "Listagem de administradores do sistema (apenas outro ADMIN pode realizar)",
            description = "Endpoint GET para a listagem de administradores disponível no sistema."
    )
    @SecurityRequirement(name = "bearer-key", scopes = { "ADMIN" })
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<DadosRetornoAdmin> listarAdmin(@PageableDefault(sort="usuario.nome", size=10) Pageable page){
        return repository.findAllbyAtivo(page).map(DadosRetornoAdmin::new);
    }

    @GetMapping("/authenticate-me")
    @Operation(
            summary = "Coleta de informações do admin autenticado",
            description = "Endpoint GET para a coleta de informaões sobre o administrador autenticado."
    )
    @SecurityRequirement(name = "bearer-key", scopes = { "ADMIN" })
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity autenticarAdmin(Authentication authentication){
        try {
            var admin = repository.findByUsuarioEmail(authentication.getName())
                    .orElseThrow(() -> new EntityNotFoundException("Perfil de administrador não encontrado para este usuário."));
            return ResponseEntity.ok(new DadosRetornoAdmin(admin));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

}

