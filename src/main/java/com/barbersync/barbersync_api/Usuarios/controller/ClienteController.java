package com.barbersync.barbersync_api.Usuarios.controller;

import com.barbersync.barbersync_api.Usuarios.dtos.DadosAlteracaoCliente;
import com.barbersync.barbersync_api.Usuarios.dtos.DadosCadastroCliente;
import com.barbersync.barbersync_api.Usuarios.dtos.DadosRetornoCliente;
import com.barbersync.barbersync_api.Usuarios.repository.ClienteRepository;
import com.barbersync.barbersync_api.Usuarios.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/auth")
public class ClienteController {

    @Autowired
    private ClienteRepository repository;

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/registrar-cliente")
    @Transactional
    public ResponseEntity registrarCliente(@RequestBody @Valid DadosCadastroCliente dados, UriComponentsBuilder uriBuilder) throws Exception {
        var cliente = clienteService.cadastrarUsuarioCliente(dados);
        var uri = uriBuilder.path("/clientes/{id}").buildAndExpand(cliente.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosRetornoCliente(cliente));
    }

    @GetMapping("/clientes")
    public Page<DadosRetornoCliente> listarClientes(@PageableDefault(size = 10, sort="usuario.nome") Pageable paginacao) {
        return repository.findAll(paginacao).map(DadosRetornoCliente::new);
    }

    @DeleteMapping("/clientes/{id}")
    @Transactional
    public ResponseEntity deletarCliente(@PathVariable Long id){
        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/alterar-cliente")
    @Transactional
    public ResponseEntity alterarCliente(@RequestBody @Valid DadosAlteracaoCliente dados) throws Exception {
        var cliente = clienteService.alterarUsuarioCliente(dados);

        return ResponseEntity.ok().body(new DadosRetornoCliente(cliente));
    }
}
