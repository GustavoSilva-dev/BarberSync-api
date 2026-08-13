package com.barbersync.barbersync_api.Usuarios.dtos;

public record DadosErro(
        String message
) {
    public DadosErro(String message){
        this.message = message;
    }
}
