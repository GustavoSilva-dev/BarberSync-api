package com.barbersync.barbersync_api.Agendamentos.components;

import com.barbersync.barbersync_api.Agendamentos.dtos.DadosCadastroAgendamento;
import com.barbersync.barbersync_api.Servicos.repository.ServicoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorServicoAtivo implements ValidadorAgendamento {

    @Autowired
    private ServicoRepository servicoRepository;

    @Override
    public void validarAgendamento(DadosCadastroAgendamento dados) throws EntityNotFoundException {
        if(dados.servicoId() == null) throw new EntityNotFoundException("Passe o ID do serviço correspondente");

        var servicoAtivo = servicoRepository.findByIdAndAtivoTrue(dados.servicoId());
        if(servicoAtivo.isEmpty()) throw new EntityNotFoundException("Serviço não encontrado ou inativo no sistema.");
    }
}
