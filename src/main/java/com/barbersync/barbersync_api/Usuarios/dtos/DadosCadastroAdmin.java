package com.barbersync.barbersync_api.Usuarios.dtos;

import jakarta.validation.constraints.*;

public record DadosCadastroAdmin(
        @NotBlank(message = "O campo nome é obrigatório.")
        String nome,

        @NotBlank(message = "O campo email é obrigatório.")
        @Email(message = "O e-mail informado deve ser válido, exemplo: usuario@dominio.com.")
        String email,

        @NotBlank(message = "O campo senha é obrigatório.")
        String senha,

        @NotNull(message = "O campo adminKey é obrigatório.")
        int adminKey
) {
}
