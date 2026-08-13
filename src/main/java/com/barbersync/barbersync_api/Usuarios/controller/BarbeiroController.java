package com.barbersync.barbersync_api.Usuarios.controller;

import com.barbersync.barbersync_api.Usuarios.dtos.DadosAlteracaoBarbeiro;
import com.barbersync.barbersync_api.Usuarios.dtos.DadosCadastroBarbeiro;
import com.barbersync.barbersync_api.Usuarios.dtos.DadosRetornoBarbeiro;
import com.barbersync.barbersync_api.Usuarios.repository.BarbeiroRepository;
import com.barbersync.barbersync_api.Usuarios.services.BarbeiroService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/barbeiros")
public class BarbeiroController {

    @Autowired
    private BarbeiroService barbeiroService;

    @Autowired
    private BarbeiroRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity registrarBarbeiro(@RequestBody @Valid DadosCadastroBarbeiro dados, UriComponentsBuilder uriBuilder) throws Exception {
        var barbeiro = barbeiroService.cadastrarUsuarioBarbeiro(dados);
        var uri = uriBuilder.path("/barbeiros/{id}").buildAndExpand(barbeiro.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosRetornoBarbeiro(barbeiro));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity excluirBarbeiro(@PathVariable Long id){
        barbeiroService.desativarBarbeiro(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @SecurityRequirement(name = "bearer-key")
    public Page<DadosRetornoBarbeiro> listarBarbeiros(@PageableDefault(size=10, sort="usuario.nome") Pageable paginacao){
        return repository.findAllByAtivo(paginacao).map(DadosRetornoBarbeiro::new);
    }

    @PutMapping
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity alterarBarbeiro(@RequestBody @Valid DadosAlteracaoBarbeiro dados) throws Exception {
        var barbeiro = barbeiroService.mudarBarbeiro(dados);
        return ResponseEntity.ok().body(new DadosRetornoBarbeiro(barbeiro));
    }
}
