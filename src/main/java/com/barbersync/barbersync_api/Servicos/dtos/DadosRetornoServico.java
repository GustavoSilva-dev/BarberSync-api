package com.barbersync.barbersync_api.Servicos.dtos;

import java.math.BigDecimal;

public record DadosRetornoServico(
        String nome,
        String descricao,
        BigDecimal preco,
        Integer duracaoEmMinutos,
        Boolean ativo
) {
}
