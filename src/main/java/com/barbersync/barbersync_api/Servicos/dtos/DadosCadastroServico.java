package com.barbersync.barbersync_api.Servicos.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

public record DadosCadastroServico(
        @NotBlank(message = "O campo de nome é obrigatório")
        String nome,

        @NotBlank(message = "O campo de descrição do serviço é obrigatório")
        String descricao,

        @NotNull(message = "O campo de preço deve ser válido")
        @Min(value = 10, message = "O preço não pode ser menor que 10 reais")
        @Max(value = 150, message = "O serviço não pode ser maior que 150 reais")
        BigDecimal preco,

        @NotNull(message = "A duração em minutos deve ser definida")
        @Min(value = 10, message = "A duração do serviço não pode ser menor que 10 minutos")
        Integer duracaoEmMinutos
) {
}
