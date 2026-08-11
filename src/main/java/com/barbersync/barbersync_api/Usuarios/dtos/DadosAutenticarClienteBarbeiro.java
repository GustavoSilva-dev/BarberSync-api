package com.barbersync.barbersync_api.Usuarios.dtos;

import jakarta.validation.constraints.NotBlank;

public record DadosAutenticarClienteBarbeiro(
        @NotBlank(message = "Email é necessário para autenticar")
        String email,
        @NotBlank(message = "Senha é necessária para autenticar")
        String senha
) {
}
