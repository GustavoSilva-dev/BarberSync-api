package com.barbersync.barbersync_api.Usuarios.controller;

import com.barbersync.barbersync_api.Usuarios.classes.Admin;
import com.barbersync.barbersync_api.Usuarios.classes.Barbeiro;
import com.barbersync.barbersync_api.Usuarios.classes.Cliente;
import com.barbersync.barbersync_api.Usuarios.dtos.DadosAutenticarAdmin;
import com.barbersync.barbersync_api.Usuarios.dtos.DadosAutenticarClienteBarbeiro;
import com.barbersync.barbersync_api.Usuarios.dtos.DadosRetornoAutenticacao;
import com.barbersync.barbersync_api.Usuarios.repository.AdminRepository;
import com.barbersync.barbersync_api.Usuarios.repository.BarbeiroRepository;
import com.barbersync.barbersync_api.Usuarios.repository.ClienteRepository;
import com.barbersync.barbersync_api.Usuarios.services.AdminService;
import com.barbersync.barbersync_api.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AdminService adminService;

    @PostMapping("/cliente")
    public ResponseEntity autenticarCliente(@Valid DadosAutenticarClienteBarbeiro dados){
        try {
            var token = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
            var autenticacao = authenticationManager.authenticate(token);
            var tokenJWT = tokenService.gerarToken((Cliente) autenticacao.getPrincipal());

            return ResponseEntity.ok(new DadosRetornoAutenticacao(tokenJWT));
        } catch (Exception e) {
            e.getStackTrace();
            return ResponseEntity.status(401).body("Falha na autenticação: " + e.getMessage());
        }
    }

    @PostMapping("/barbeiro")
    public ResponseEntity autenticarBarbeiro(@Valid DadosAutenticarClienteBarbeiro dados){
        try {
            var token = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
            var autenticacao = authenticationManager.authenticate(token);
            var tokenJWT = tokenService.gerarToken((Barbeiro) autenticacao.getPrincipal());

            return ResponseEntity.ok(new DadosRetornoAutenticacao(tokenJWT));
        } catch (Exception e) {
            e.getStackTrace();
            return ResponseEntity.status(401).body("Falha na autenticação: " + e.getMessage());
        }

    }

    @PostMapping("/admin")
    public ResponseEntity autenticarAdmin(@Valid DadosAutenticarAdmin dados){
        try {
            boolean validador = adminService.validarKey(dados.adminKey());

            if(validador) {
                var token = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
                var autenticacao = authenticationManager.authenticate(token);
                var tokenJWT = tokenService.gerarToken((Admin) autenticacao.getPrincipal());

                return ResponseEntity.ok(new DadosRetornoAutenticacao(tokenJWT));
            } else {
                throw new Exception("Chave de admin inválida.");
            }
        } catch (Exception e) {
            e.getStackTrace();
            return ResponseEntity.status(401).body("Falha na autenticação: " + e.getMessage());
        }

    }
}
