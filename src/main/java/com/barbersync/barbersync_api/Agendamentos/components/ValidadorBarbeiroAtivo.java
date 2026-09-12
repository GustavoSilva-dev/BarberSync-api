package com.barbersync.barbersync_api.Agendamentos.components;

import com.barbersync.barbersync_api.Agendamentos.dtos.DadosCadastroAgendamento;
import com.barbersync.barbersync_api.Usuarios.classes.Barbeiro;
import com.barbersync.barbersync_api.Usuarios.controller.BarbeiroController;
import com.barbersync.barbersync_api.Usuarios.repository.BarbeiroRepository;
import com.barbersync.barbersync_api.infra.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorBarbeiroAtivo implements ValidadorAgendamento {

    @Autowired
    private BarbeiroRepository barbeiroRepository;

    @Override
    public void validarAgendamento(DadosCadastroAgendamento dados) {
        if(dados.barbeiroId() == null){
            return;
        }

        var barbeiroAtivo = barbeiroRepository.findByAtivo(dados.barbeiroId());
        if(barbeiroAtivo == null){
            throw new ValidacaoException("Barbeiro não encontrado ou inativo no sistema.");
        }
    }
}
