package com.barbersync.barbersync_api.Servicos.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

public record DadosDetalhamentoServico(
        String nome,
        String descricao,
        BigDecimal preco,
        Integer duracaoEmMinutos,
        Boolean ativo
) {
}
