package com.barbersync.barbersync_api.Agendamentos.components;

import com.barbersync.barbersync_api.Agendamentos.dtos.DadosCadastroAgendamento;
import com.barbersync.barbersync_api.Servicos.classes.Servico;
import com.barbersync.barbersync_api.Servicos.repository.ServicoRepository;
import com.barbersync.barbersync_api.infra.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorServicoAtivo implements ValidadorAgendamento {

    @Autowired
    private ServicoRepository servicoRepository;

    @Override
    public void validarAgendamento(DadosCadastroAgendamento dados) {
        if(dados.servicoId() == null){
            return;
        }

        var servicoAtivo = servicoRepository.findByIdAndAtivoTrue(dados.servicoId());
        if(servicoAtivo.isEmpty()){
            throw new ValidacaoException("Serviço não encontrado ou inativo no sistema.");
        }
    }
}
