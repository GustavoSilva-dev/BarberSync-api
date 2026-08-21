package com.barbersync.barbersync_api.Usuarios.controller;

import com.barbersync.barbersync_api.Usuarios.classes.Cliente;
import com.barbersync.barbersync_api.Usuarios.classes.Usuario;
import com.barbersync.barbersync_api.Usuarios.dtos.DadosAlteracaoCliente;
import com.barbersync.barbersync_api.Usuarios.dtos.DadosCadastroCliente;
import com.barbersync.barbersync_api.Usuarios.dtos.DadosEnviarToken;
import com.barbersync.barbersync_api.Usuarios.dtos.DadosRetornoCliente;
import com.barbersync.barbersync_api.Usuarios.repository.ClienteRepository;
import com.barbersync.barbersync_api.Usuarios.services.ClienteService;
import com.barbersync.barbersync_api.infra.security.TokenService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository repository;

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    @Transactional
    public ResponseEntity registrarCliente(@RequestBody @Valid DadosCadastroCliente dados, UriComponentsBuilder uriBuilder) throws Exception {
        var cliente = clienteService.cadastrarUsuarioCliente(dados);
        var uri = uriBuilder.path("/clientes/{id}").buildAndExpand(cliente.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosRetornoCliente(cliente));
    }

    @GetMapping
    @SecurityRequirement(name = "bearer-key")
    public Page<DadosRetornoCliente> listarClientes(@PageableDefault(size = 10, sort="usuario.nome") Pageable paginacao) {
        return repository.findAll(paginacao).map(DadosRetornoCliente::new);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity deletarCliente(@PathVariable Long id){
        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @Transactional
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity alterarCliente(@RequestBody @Valid DadosAlteracaoCliente dados) throws Exception {
        var cliente = clienteService.alterarUsuarioCliente(dados);

        return ResponseEntity.ok().body(new DadosRetornoCliente(cliente));
    }

    @GetMapping("/authenticate-me")
    @SecurityRequirement(name = "BearerAuth", scopes = { "ROLE_CLIENTE" })
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity autenticarCliente(Authentication authentication){
        var cliente = (Cliente) repository.findByUsuarioEmail(authentication.getName());
        return ResponseEntity.ok(new DadosRetornoCliente(cliente));
    }
}
