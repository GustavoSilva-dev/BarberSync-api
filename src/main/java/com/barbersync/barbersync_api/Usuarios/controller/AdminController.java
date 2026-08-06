package com.barbersync.barbersync_api.Usuarios.controller;

import com.barbersync.barbersync_api.Usuarios.dtos.DadosAlteracaoAdmin;
import com.barbersync.barbersync_api.Usuarios.dtos.DadosCadastroAdmin;
import com.barbersync.barbersync_api.Usuarios.dtos.DadosRetornoBarbeiro;
import com.barbersync.barbersync_api.Usuarios.repository.AdminRepository;
import com.barbersync.barbersync_api.Usuarios.services.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity registrarAdmin(@RequestBody @Valid DadosCadastroAdmin dados) throws Exception {
        adminService.cadastrarUsuarioAdmin(dados);

        return ResponseEntity.ok().build();
    }

    @PutMapping
    @Transactional
    public ResponseEntity alterarAdmin(@RequestBody @Valid DadosAlteracaoAdmin dados) throws Exception {
        adminService.alterarUsuarioAdmin(dados);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluirBarbeiro(@PathVariable Long id) {
        adminService.desativarAdmin(id);

        return ResponseEntity.noContent().build();
    }
}

