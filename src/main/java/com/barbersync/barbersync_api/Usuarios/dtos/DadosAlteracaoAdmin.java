package com.barbersync.barbersync_api.Usuarios.dtos;

import jakarta.validation.constraints.NotNull;

public record DadosAlteracaoAdmin(
        @NotNull
        Long id,

        String nome,
        String email,
        String senha,
        int adminKey
) {
}
