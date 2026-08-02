package com.barbersync.barbersync_api.Usuarios.dtos;

import jakarta.validation.Valid;

public record DadosRetornoUsuario(
        String nome,
        String email,
        String telefone,
        Roles acesso
) {
    public DadosRetornoUsuario(@Valid DadosCadastroCliente dados) {
        this(dados.nome(), dados.email(), dados.telefone(), Roles.CLIENTE);
    }
}
