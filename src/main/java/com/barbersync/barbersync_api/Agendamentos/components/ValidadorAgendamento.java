package com.barbersync.barbersync_api.Agendamentos.components;

import com.barbersync.barbersync_api.Agendamentos.dtos.DadosCadastroAgendamento;

public interface ValidadorAgendamento {
    void validarAgendamento(DadosCadastroAgendamento dados);
}
