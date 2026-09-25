package com.barbersync.barbersync_api.Email.dtos;

public record DadosEmail(
        String to,
        String subject,
        String body
) {
}
