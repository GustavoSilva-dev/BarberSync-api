package com.barbersync.barbersync_api.Usuarios.dtos;

import jakarta.validation.Valid;

public record DadosRetornoCliente(
        String nome,
        String email,
        String telefone,
        Roles acesso
) {
    public DadosRetornoCliente(@Valid DadosCadastroCliente dados) {
        this(dados.nome(), dados.email(), dados.telefone(), Roles.CLIENTE);
    }
}
