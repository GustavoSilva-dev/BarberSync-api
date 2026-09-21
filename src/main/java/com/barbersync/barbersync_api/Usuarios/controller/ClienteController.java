package com.barbersync.barbersync_api.Usuarios.controller;

import com.barbersync.barbersync_api.Usuarios.classes.Cliente;
import com.barbersync.barbersync_api.Usuarios.dtos.DadosAlteracaoCliente;
import com.barbersync.barbersync_api.Usuarios.dtos.DadosCadastroCliente;
import com.barbersync.barbersync_api.Usuarios.dtos.DadosRetornoCliente;
import com.barbersync.barbersync_api.Usuarios.repository.ClienteRepository;
import com.barbersync.barbersync_api.Usuarios.services.ClienteService;
import com.barbersync.barbersync_api.infra.exception.ValidacaoException;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.security.authorization.AuthorityReactiveAuthorizationManager.hasAnyAuthority;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository repository;

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    @Operation(
            summary = "Cadastro de um cliente do sistema",
            description = "Endpoint POST para o cadastro de um admin do sistema do BarberSync, com dados de um usuário completo (nome, email, senha)."
    )
    @Transactional
    public ResponseEntity registrarCliente(@RequestBody @Valid DadosCadastroCliente dados, UriComponentsBuilder uriBuilder) throws Exception {
        var cliente = clienteService.cadastrarUsuarioCliente(dados);
        var uri = uriBuilder.path("/clientes/{id}").buildAndExpand(cliente.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosRetornoCliente(cliente));
    }

    @GetMapping
    @Operation(
            summary = "Listagem de clientes do sistema",
            description = "Endpoint GET para a listagem de todos os clientes ativos do sistema do BarberSync, com dados completos (nome, email, etc)."
    )
    public Page<DadosRetornoCliente> listarClientes(@PageableDefault(size = 10, sort="usuario.nome") Pageable paginacao) {
        return repository.findAllByAtivo(paginacao).map(DadosRetornoCliente::new);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(
            summary = "Exclusão de um cliente do sistema",
            description = "Endpoint DELETE para a (safe delete) de um cliente ativo no sistema, alternando seu status de atividade para FALSE"
    )
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity deletarCliente(@PathVariable Long id){
        clienteService.deletarUsuarioCliente(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @Transactional
    @Operation(
            summary = "Edição de um cliente do sistema",
            description = "Endpoint PUT para a edição de um cliente ativo no sistema, alternando seu dados cadastrados."
    )
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity alterarCliente(@RequestBody @Valid DadosAlteracaoCliente dados) throws Exception {
        var cliente = clienteService.alterarUsuarioCliente(dados);

        return ResponseEntity.ok().body(new DadosRetornoCliente(cliente));
    }

    @GetMapping("/authenticate-me")
    @Operation (
            summary = "Retorno das informações do cliente autenticado",
            description = "Endpoint POST para coletar as informações cadastrais do usuário 'cliente' autenticado no sistema do BarberSync."
    )
    @SecurityRequirement(name = "bearer-key", scopes = {"CLIENTE", "ADMIN"})
    @PreAuthorize("hasAuthority('CLIENTE')")
    public ResponseEntity autenticarCliente(Authentication authentication){
        try {
            var cliente = repository.findByUsuarioEmail(authentication.getName())
                    .orElseThrow(() -> new EntityNotFoundException("Perfil de cliente não encontrado para este usuário."));

            return ResponseEntity.ok().body(new DadosRetornoCliente(cliente));
        } catch (Exception e) {
            throw new UsernameNotFoundException("Usuário não autenticado");
        }
    }
}
