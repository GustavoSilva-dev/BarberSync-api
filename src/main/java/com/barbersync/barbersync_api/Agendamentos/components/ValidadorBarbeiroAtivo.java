package com.barbersync.barbersync_api.Agendamentos.components;

import com.barbersync.barbersync_api.Agendamentos.dtos.DadosCadastroAgendamento;
import com.barbersync.barbersync_api.Usuarios.repository.BarbeiroRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorBarbeiroAtivo implements ValidadorAgendamento {

    @Autowired
    private BarbeiroRepository barbeiroRepository;

    @Override
    public void validarAgendamento(DadosCadastroAgendamento dados) throws EntityNotFoundException {
        if(dados.barbeiroId() == null) throw new EntityNotFoundException("Passe o ID do barbeiro correspondente");

        var barbeiroAtivo = barbeiroRepository.findByAtivo(dados.barbeiroId());
        if(barbeiroAtivo == null) throw new EntityNotFoundException("Barbeiro não encontrado ou inativo no sistema.");
    }
}
