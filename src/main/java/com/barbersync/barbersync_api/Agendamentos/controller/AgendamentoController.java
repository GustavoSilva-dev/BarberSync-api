package com.barbersync.barbersync_api.Agendamentos.controller;

import com.barbersync.barbersync_api.Agendamentos.dtos.AgendamentoClientSide;
import com.barbersync.barbersync_api.Agendamentos.dtos.DadosCadastroAgendamento;
import com.barbersync.barbersync_api.Agendamentos.dtos.DadosRetornoAgendamento;
import com.barbersync.barbersync_api.Agendamentos.repository.AgendamentoRepository;
import com.barbersync.barbersync_api.Agendamentos.services.AgendamentoService;
import com.barbersync.barbersync_api.Servicos.dtos.DadosRetornoServico;
import com.barbersync.barbersync_api.Usuarios.classes.Cliente;
import com.barbersync.barbersync_api.Usuarios.classes.Usuario;
import com.barbersync.barbersync_api.Usuarios.dtos.DadosRetornoBarbeiro;
import com.barbersync.barbersync_api.Usuarios.dtos.DadosRetornoCliente;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService service;

    @PostMapping("/admin-side")
    @Operation(
            summary = "Criar novo AGENDAMENTO - Feito por ADMIN ou BARBEIRO",
            description = "Endpoint POST para criar novos agendamentos, com validadores de negócio, realizado pessoalmente pelo ADMIN ou pelo BARBEIRO responsável."
    )
    @SecurityRequirement(name = "bearer-key", scopes = {"ADMIN", "BARBEIRO"})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BARBEIRO')")
    @Transactional
    public ResponseEntity cadastrarAgendamentoAdmin(@RequestBody @Valid DadosCadastroAgendamento dados, UriComponentsBuilder uriBuilder){
        var agendamento = service.validarDadosAgendamento(dados);
        var uri = uriBuilder.path("/agendamentos/{id}").buildAndExpand(agendamento.getId()).toUri();

        return ResponseEntity.created(uri).body(
                new DadosRetornoAgendamento(
                        agendamento.getDataHoraInicio(),
                        agendamento.getDataHoraFinal(),
                        new DadosRetornoCliente(
                                agendamento.getCliente().getUsuario().getNome(),
                                agendamento.getCliente().getUsuario().getEmail(),
                                agendamento.getCliente().getTelefone(),
                                agendamento.getCliente().getUsuario().getRole()
                        ),
                        new DadosRetornoBarbeiro(
                                agendamento.getBarbeiro().getUsuario().getNome(),
                                agendamento.getBarbeiro().getUsuario().getEmail(),
                                agendamento.getBarbeiro().getTelefone(),
                                agendamento.getBarbeiro().getCpf(),
                                agendamento.getBarbeiro().getUsuario().getRole()
                        ),
                        new DadosRetornoServico(
                                agendamento.getServico().getNome(),
                                agendamento.getServico().getDescricao(),
                                agendamento.getServico().getPreco(),
                                agendamento.getServico().getDuracaoEmMinutos(),
                                agendamento.getServico().getAtivo()
                        )
                )
        );
    }

    @PostMapping("/client-side")
    @Operation(
            summary = "Criar novo AGENDAMENTO - Feito por CLIENTE",
            description = "Endpoint POST para criar novos agendamentos, realizado pelo CLIENTE logado, coletando ID pelo Spring Security."
    )
    @SecurityRequirement(name = "bearer-key", scopes = {"ADMIN", "BARBEIRO"})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'BARBEIRO')")
    @Transactional
    public ResponseEntity cadastrarAgendamentoCliente(@RequestBody @Valid AgendamentoClientSide dados, @AuthenticationPrincipal Cliente clienteAutenticado, UriComponentsBuilder uriBuilder){

        DadosCadastroAgendamento dadosCadastro = new DadosCadastroAgendamento(
                dados.dataHoraInicio(),
                dados.barbeiroId(),
                clienteAutenticado.getId(),
                dados.servicoId()
        );

        var agendamento = service.validarDadosAgendamento(dadosCadastro);
        var uri = uriBuilder.path("/agendamentos/{id}").buildAndExpand(agendamento.getId()).toUri();

        return ResponseEntity.created(uri).body(
                new DadosRetornoAgendamento(
                        agendamento.getDataHoraInicio(),
                        agendamento.getDataHoraFinal(),
                        new DadosRetornoCliente(
                                agendamento.getCliente().getUsuario().getNome(),
                                agendamento.getCliente().getUsuario().getEmail(),
                                agendamento.getCliente().getTelefone(),
                                agendamento.getCliente().getUsuario().getRole()
                        ),
                        new DadosRetornoBarbeiro(
                                agendamento.getBarbeiro().getUsuario().getNome(),
                                agendamento.getBarbeiro().getUsuario().getEmail(),
                                agendamento.getBarbeiro().getTelefone(),
                                agendamento.getBarbeiro().getCpf(),
                                agendamento.getBarbeiro().getUsuario().getRole()
                        ),
                        new DadosRetornoServico(
                                agendamento.getServico().getNome(),
                                agendamento.getServico().getDescricao(),
                                agendamento.getServico().getPreco(),
                                agendamento.getServico().getDuracaoEmMinutos(),
                                agendamento.getServico().getAtivo()
                        )
                )
        );
    }

    @PatchMapping("/concluir/{id}")
    @Transactional
    public ResponseEntity alterarAgendamentoConcluido(@PathVariable Long id){
        var agendamentoExistente = service.validarAgendamentoConcluido(id);
        var dadosRetorno = new DadosRetornoAgendamento(
                agendamentoExistente.getDataHoraInicio(),
                agendamentoExistente.getDataHoraFinal(),
                new DadosRetornoCliente(
                        agendamentoExistente.getCliente().getUsuario().getNome(),
                        agendamentoExistente.getCliente().getUsuario().getEmail(),
                        agendamentoExistente.getCliente().getTelefone(),
                        agendamentoExistente.getCliente().getUsuario().getRole()
                ),
                new DadosRetornoBarbeiro(
                        agendamentoExistente.getBarbeiro().getUsuario().getNome(),
                        agendamentoExistente.getBarbeiro().getUsuario().getEmail(),
                        agendamentoExistente.getBarbeiro().getTelefone(),
                        agendamentoExistente.getBarbeiro().getCpf(),
                        agendamentoExistente.getBarbeiro().getUsuario().getRole()
                ),
                new DadosRetornoServico(
                        agendamentoExistente.getServico().getNome(),
                        agendamentoExistente.getServico().getDescricao(),
                        agendamentoExistente.getServico().getPreco(),
                        agendamentoExistente.getServico().getDuracaoEmMinutos(),
                        agendamentoExistente.getServico().getAtivo()
                )
        );

        return ResponseEntity.ok().body(dadosRetorno);
    }
}
