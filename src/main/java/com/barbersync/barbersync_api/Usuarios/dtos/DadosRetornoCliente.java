package com.barbersync.barbersync_api.Usuarios.dtos;

import com.barbersync.barbersync_api.Usuarios.classes.Cliente;
import jakarta.validation.Valid;

public record DadosRetornoCliente(
        String nome,
        String email,
        String telefone,
        Roles acesso
) {
    public DadosRetornoCliente(Cliente cliente) {
        this(cliente.getUsuario().getNome(), cliente.getUsuario().getEmail(), cliente.getTelefone(), Roles.CLIENTE);
    }
}
