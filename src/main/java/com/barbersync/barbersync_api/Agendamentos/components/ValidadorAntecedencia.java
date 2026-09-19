package com.barbersync.barbersync_api.Agendamentos.components;

import com.barbersync.barbersync_api.Agendamentos.dtos.DadosCadastroAgendamento;
import com.barbersync.barbersync_api.infra.exception.ValidacaoException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ValidadorAntecedencia implements ValidadorAgendamento {

    @Override
    public void validarAgendamento(DadosCadastroAgendamento dados) {
        boolean antecedenciaValida = dados.dataHoraInicio().isAfter(LocalDateTime.now().plusMinutes(30));

        if(!antecedenciaValida) throw new ValidacaoException("HORÁRIO INVÁLIDO: O agendamento precisa ser feito pelo menos com 30 minutos de antecedência.");
    }
}
