package com.barbersync.barbersync_api.Usuarios.controller;

import com.barbersync.barbersync_api.Usuarios.dtos.DadosCadastroCliente;
import com.barbersync.barbersync_api.Usuarios.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UsuariosController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/registrar-cliente")
    public ResponseEntity registrarCliente(@RequestBody @Valid DadosCadastroCliente dados){
        clienteService.cadastrarUsuarioCliente(dados);

        return ResponseEntity.ok().build();
    }
}
