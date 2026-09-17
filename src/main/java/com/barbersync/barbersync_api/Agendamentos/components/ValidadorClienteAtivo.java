package com.barbersync.barbersync_api.Agendamentos.components;

import com.barbersync.barbersync_api.Agendamentos.dtos.DadosCadastroAgendamento;
import com.barbersync.barbersync_api.Usuarios.repository.ClienteRepository;
import com.barbersync.barbersync_api.infra.exception.ValidacaoException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorClienteAtivo implements ValidadorAgendamento {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public void validarAgendamento(DadosCadastroAgendamento dados) throws EntityNotFoundException {
        if(dados.clienteId() == null) throw new EntityNotFoundException("Passe o ID do cliente correspondente");

        var cliente = clienteRepository.findByAtivo(dados.clienteId());
        if(cliente == null) throw new EntityNotFoundException("Cliente não encontrado");
    }
}
