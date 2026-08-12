package com.barbersync.barbersync_api.Usuarios.dtos;

import jakarta.validation.constraints.NotBlank;

public record DadosAutenticarAdmin(
        @NotBlank(message = "Email é necessário para autenticar")
        String email,
        @NotBlank(message = "Senha é necessária para autenticar")
        String senha,
        @NotBlank(message = "Insira a chave de admin para autenticar")
        String adminKey
) {
}
