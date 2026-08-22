package com.barbersync.barbersync_api.Usuarios.controller;

import com.barbersync.barbersync_api.Usuarios.classes.Barbeiro;
import com.barbersync.barbersync_api.Usuarios.classes.Cliente;
import com.barbersync.barbersync_api.Usuarios.dtos.DadosAlteracaoBarbeiro;
import com.barbersync.barbersync_api.Usuarios.dtos.DadosCadastroBarbeiro;
import com.barbersync.barbersync_api.Usuarios.dtos.DadosRetornoBarbeiro;
import com.barbersync.barbersync_api.Usuarios.dtos.DadosRetornoCliente;
import com.barbersync.barbersync_api.Usuarios.repository.BarbeiroRepository;
import com.barbersync.barbersync_api.Usuarios.services.BarbeiroService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    @SecurityRequirement(name = "bearer-key", scopes = { "BARBEIRO", "ADMIN" })
    @PreAuthorize("hasAnyAuthority('BARBEIRO', 'ADMIN')")
    public ResponseEntity excluirBarbeiro(@PathVariable Long id){
        barbeiroService.desativarBarbeiro(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @SecurityRequirement(name = "bearer-key", scopes = { "BARBEIRO", "ADMIN" })
    @PreAuthorize("hasAnyAuthority('BARBEIRO', 'ADMIN')")
    public Page<DadosRetornoBarbeiro> listarBarbeiros(@PageableDefault(size=10, sort="usuario.nome") Pageable paginacao){
        return repository.findAllByAtivo(paginacao).map(DadosRetornoBarbeiro::new);
    }

    @PutMapping
    @SecurityRequirement(name = "bearer-key", scopes = { "BARBEIRO", "ADMIN" })
    @PreAuthorize("hasAnyAuthority('BARBEIRO', 'ADMIN')")
    public ResponseEntity alterarBarbeiro(@RequestBody @Valid DadosAlteracaoBarbeiro dados) throws Exception {
        var barbeiro = barbeiroService.mudarBarbeiro(dados);
        return ResponseEntity.ok().body(new DadosRetornoBarbeiro(barbeiro));
    }

    @GetMapping("/authenticate-me")
    @SecurityRequirement(name = "bearer-key", scopes = { "BARBEIRO", "ADMIN" })
    @PreAuthorize("hasAnyAuthority('BARBEIRO', 'ADMIN')")
    public ResponseEntity autenticarBarbeiro(Authentication authentication){
        try {
            var barbeiro = (Barbeiro) repository.findByUsuarioEmail(authentication.getName());
            return ResponseEntity.ok(new DadosRetornoBarbeiro(barbeiro));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
