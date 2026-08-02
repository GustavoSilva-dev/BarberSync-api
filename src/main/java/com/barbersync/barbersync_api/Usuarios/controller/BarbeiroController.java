package com.barbersync.barbersync_api.Usuarios.controller;

import com.barbersync.barbersync_api.Usuarios.dtos.DadosCadastroBarbeiro;
import com.barbersync.barbersync_api.Usuarios.services.BarbeiroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class BarbeiroController {

    @Autowired
    private BarbeiroService barbeiroService;

    @PostMapping("/registrar-barbeiro")
    @Transactional
    public ResponseEntity registrarBarbeiro(@RequestBody @Valid DadosCadastroBarbeiro dados) throws Exception {
        barbeiroService.cadastrarUsuarioBarbeiro(dados);

        return ResponseEntity.ok().build();
    }
}
