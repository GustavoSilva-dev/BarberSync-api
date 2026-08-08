package com.barbersync.barbersync_api.Usuarios.dtos;

import jakarta.validation.constraints.NotNull;

public record DadosAlteracaoAdmin(
        @NotNull(message = "O campo id é obrigatório.")
        Long id,

        String nome,
        String email,
        String senha,
        int adminKey
) {
}
