package com.barbersync.barbersync_api.Usuarios.controller;

import com.barbersync.barbersync_api.Usuarios.dtos.DadosCadastroCliente;
import com.barbersync.barbersync_api.Usuarios.dtos.DadosRetornoCliente;
import com.barbersync.barbersync_api.Usuarios.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/registrar-cliente")
    @Transactional
    public ResponseEntity registrarCliente(@RequestBody @Valid DadosCadastroCliente dados) throws Exception {
        clienteService.cadastrarUsuarioCliente(dados);

        return ResponseEntity.ok().body(new DadosRetornoCliente(dados));
    }
}
