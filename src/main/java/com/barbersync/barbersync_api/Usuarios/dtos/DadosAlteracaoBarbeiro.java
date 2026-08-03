package com.barbersync.barbersync_api.Usuarios.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosAlteracaoBarbeiro(
        @NotNull
        Long id,

        String nome,
        String email,
        String senha,

        @Pattern(regexp = "^\\d{11}$", message = "O CPF deve conter exatamente 11 dígitos")
        String cpf,

        @Pattern(regexp = "^\\d{11}$", message = "O telefone deve conter exatamente 11 dígitos")
        String telefone
) {
}
