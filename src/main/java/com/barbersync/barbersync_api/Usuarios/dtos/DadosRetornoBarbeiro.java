package com.barbersync.barbersync_api.Usuarios.dtos;

import com.barbersync.barbersync_api.Usuarios.classes.Barbeiro;
import jakarta.validation.Valid;

public record DadosRetornoBarbeiro(
        String nome,
        String email,
        String telefone,
        String cpf,
        Roles acesso
) {
    public DadosRetornoBarbeiro(Barbeiro barbeiro) {
        this(barbeiro.getUsuario().getNome(), barbeiro.getUsuario().getEmail(), barbeiro.getTelefone(), barbeiro.getCpf(), barbeiro.getUsuario().getRole());
    }
}
