package com.barbersync.barbersync_api.Usuarios.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosAlteracaoCliente(
        @NotNull(message = "O campo id é obrigatório.")
        Long id,

        String nome,

        @Email(message = "O e-mail informado deve ser válido, exemplo: usuario@dominio.com.")
        String email,

        String senha,

        @Pattern(regexp = "^\\d{11}$", message = "O campo telefone possui um formato inválido.")
        String telefone
) {
}
