package com.barbersync.barbersync_api.Agendamentos.controller;

import com.barbersync.barbersync_api.Agendamentos.dtos.DadosCadastroAgendamento;
import com.barbersync.barbersync_api.Agendamentos.repository.AgendamentoRepository;
import com.barbersync.barbersync_api.Agendamentos.services.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService service;

    @Autowired
    private AgendamentoRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrarAgendamento(@RequestBody @Valid DadosCadastroAgendamento dados){
        var agendamento = service.validarDadosAgendamento(dados);
        repository.save(agendamento);
        return ResponseEntity.ok().build();
    }
}
