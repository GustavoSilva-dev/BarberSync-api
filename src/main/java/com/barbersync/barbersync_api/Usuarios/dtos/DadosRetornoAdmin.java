package com.barbersync.barbersync_api.Usuarios.dtos;

import com.barbersync.barbersync_api.Usuarios.classes.Admin;
import jakarta.validation.Valid;

public record DadosRetornoAdmin(
        String nome,
        String email,
        Roles acesso
) {
    public DadosRetornoAdmin(Admin admin) {
        this(admin.getUsuario().getNome(), admin.getUsuario().getEmail(), Roles.ADMIN);
    }
}
