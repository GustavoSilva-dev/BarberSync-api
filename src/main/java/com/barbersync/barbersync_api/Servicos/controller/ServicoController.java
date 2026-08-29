package com.barbersync.barbersync_api.Servicos.controller;

import com.barbersync.barbersync_api.Servicos.dtos.DadosAlterarServico;
import com.barbersync.barbersync_api.Servicos.dtos.DadosCadastroServico;
import com.barbersync.barbersync_api.Servicos.dtos.DadosDetalhamentoServico;
import com.barbersync.barbersync_api.Servicos.services.ServicoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/servico")
public class ServicoController {

    @Autowired
    private ServicoService servicoService;

    @GetMapping
    public Page<DadosDetalhamentoServico> listarServicos(@PageableDefault(size = 10, sort = "nome") Pageable paginacao) {
        return servicoService.listarServicosAtivos(paginacao)
                .map(servico -> new DadosDetalhamentoServico(
                        servico.getNome(),
                        servico.getDescricao(),
                        servico.getPreco(),
                        servico.getDuracaoEmMinutos(),
                        servico.getAtivo()
                ));
    }

    @PostMapping
    @Transactional
    @SecurityRequirement(name = "bearer-key", scopes = {"BARBEIRO", "ADMIN"})
    @PreAuthorize("hasAnyAuthority('BARBEIRO', 'ADMIN')")
    public ResponseEntity<DadosDetalhamentoServico> cadastrarServico(@RequestBody @Valid DadosCadastroServico dados, UriComponentsBuilder uriBuilder) {
        var servico = servicoService.cadastrarServico(dados);
        var uri = uriBuilder.path("/servico/{id}").buildAndExpand(servico.getId()).toUri();

        var detalhamento = new DadosDetalhamentoServico(
                servico.getNome(),
                servico.getDescricao(),
                servico.getPreco(),
                servico.getDuracaoEmMinutos(),
                servico.getAtivo()
        );

        return ResponseEntity.created(uri).body(detalhamento);
    }

    @PutMapping("/{id}")
    @Transactional
    @SecurityRequirement(name = "bearer-key", scopes = {"BARBEIRO", "ADMIN"})
    @PreAuthorize("hasAnyAuthority('BARBEIRO', 'ADMIN')")
    public ResponseEntity<DadosDetalhamentoServico> alterarServico(@PathVariable Long id, @RequestBody @Valid DadosAlterarServico dados) {
        var servico = servicoService.alterarServico(id, dados);

        var detalhamento = new DadosDetalhamentoServico(
                servico.getNome(),
                servico.getDescricao(),
                servico.getPreco(),
                servico.getDuracaoEmMinutos(),
                servico.getAtivo()
        );

        return ResponseEntity.ok(detalhamento);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @SecurityRequirement(name = "bearer-key", scopes = {"BARBEIRO", "ADMIN"})
    @PreAuthorize("hasAnyAuthority('BARBEIRO', 'ADMIN')")
    public ResponseEntity<Void> excluirServico(@PathVariable Long id) {
        servicoService.excluirServico(id);
        return ResponseEntity.noContent().build();
    }
}
