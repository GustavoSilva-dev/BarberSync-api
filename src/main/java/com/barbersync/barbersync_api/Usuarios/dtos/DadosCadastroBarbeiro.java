package com.barbersync.barbersync_api.Usuarios.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DadosCadastroBarbeiro(
        @NotBlank(message = "O campo nome é obrigatório.")
        String nome,
        @NotBlank(message = "O campo email é obrigatório.")
        @Email(message = "O e-mail informado deve ser válido, exemplo: usuario@dominio.com.")
        String email,
        @NotBlank(message = "O campo senha é obrigatório.")
        String senha,

        // Telefone é Obrigatório
        @NotBlank(message = "O campo telefone é obrigatório.")
        @Pattern(regexp = "^\\d{11}$", message = "O campo telefone possui um formato inválido.")
        String telefone,

        // CPF é Obrigatório
        @NotBlank(message = "O campo cpf é obrigatório.")
        @Pattern(regexp = "^\\d{11}$", message = "O campo cpf possui um formato inválido.")
        String cpf
) {
}
