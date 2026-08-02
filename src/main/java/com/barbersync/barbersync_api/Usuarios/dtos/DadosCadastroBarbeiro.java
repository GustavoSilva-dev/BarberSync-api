package com.barbersync.barbersync_api.Usuarios.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DadosCadastroBarbeiro(
        @NotBlank
        String nome,
        @NotBlank @Email
        String email,
        @NotBlank
        String senha,

        // Telefone é Obrigatório
        @NotBlank
        @Pattern(regexp = "^\\d{11}$", message = "O telefone deve conter exatamente 11 dígitos")
        String telefone,

        // CPF é Obrigatório
        @NotBlank
        @Pattern(regexp = "^\\d{11}$")
        String cpf
) {
}
