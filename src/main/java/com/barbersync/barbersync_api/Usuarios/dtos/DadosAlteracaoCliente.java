package com.barbersync.barbersync_api.Usuarios.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosAlteracaoCliente(
        @NotNull
        Long id,
        String nome,
        @Email
        String email,
        String senha,
        @Pattern(regexp = "^\\d{11}$", message = "O telefone deve conter exatamente 11 dígitos")
        String telefone
) {
}
