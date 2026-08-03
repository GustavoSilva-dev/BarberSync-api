package com.barbersync.barbersync_api.Usuarios.controller;

import com.barbersync.barbersync_api.Usuarios.dtos.DadosAlteracaoBarbeiro;
import com.barbersync.barbersync_api.Usuarios.dtos.DadosCadastroBarbeiro;
import com.barbersync.barbersync_api.Usuarios.dtos.DadosRetornoBarbeiro;
import com.barbersync.barbersync_api.Usuarios.repository.BarbeiroRepository;
import com.barbersync.barbersync_api.Usuarios.services.BarbeiroService;
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
@RequestMapping("/auth")
public class BarbeiroController {

    @Autowired
    private BarbeiroService barbeiroService;

    @Autowired
    private BarbeiroRepository repository;

    @PostMapping("/registrar-barbeiro")
    @Transactional
    public ResponseEntity registrarBarbeiro(@RequestBody @Valid DadosCadastroBarbeiro dados, UriComponentsBuilder uriBuilder) throws Exception {
        var barbeiro = barbeiroService.cadastrarUsuarioBarbeiro(dados);
        var uri = uriBuilder.path("/barbeiros/{id}").buildAndExpand(barbeiro.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosRetornoBarbeiro(barbeiro));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluirBarbeiro(@PathVariable Long id){
        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/barbeiros")
    public Page<DadosRetornoBarbeiro> listarBarbeiros(@PageableDefault(size=10, sort="nome") Pageable paginacao){
        return repository.findAll(paginacao).map(DadosRetornoBarbeiro::new);
    }

    @PutMapping("/alterar-barbeiro")
    public ResponseEntity alterarBarbeiro(@RequestBody @Valid DadosAlteracaoBarbeiro dados) throws Exception {
        var barbeiro = barbeiroService.mudarBarbeiro(dados);
        return ResponseEntity.ok().body(new DadosRetornoBarbeiro(barbeiro));
    }
}
