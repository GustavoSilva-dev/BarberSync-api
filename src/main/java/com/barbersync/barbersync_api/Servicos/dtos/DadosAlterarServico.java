package com.barbersync.barbersync_api.Servicos.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DadosAlterarServico(
        String nome,

        String descricao,

        @Min(value = 1, message = "O preço não pode ser menor que 10 reais")
        @Max(value = 150, message = "O serviço não pode ser maior que 150 reais")
        BigDecimal preco,

        @Min(value = 10, message = "A duração do serviço não pode ser menor que 10 minutos")
        Integer duracaoEmMinutos
) {
}
