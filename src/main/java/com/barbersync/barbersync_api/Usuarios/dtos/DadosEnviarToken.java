package com.barbersync.barbersync_api.Usuarios.dtos;

public record DadosEnviarToken(
        String token
) {
    public DadosEnviarToken(String token){
        this.token = token;
    }
}
