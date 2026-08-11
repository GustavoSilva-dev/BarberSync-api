package com.barbersync.barbersync_api.Usuarios.services;

import com.barbersync.barbersync_api.Usuarios.classes.Barbeiro;
import com.barbersync.barbersync_api.Usuarios.classes.Usuario;
import com.barbersync.barbersync_api.Usuarios.dtos.*;
import com.barbersync.barbersync_api.Usuarios.repository.BarbeiroRepository;
import com.barbersync.barbersync_api.Usuarios.repository.UsuariosRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BarbeiroService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private BarbeiroRepository barbeiroRepository;

    @Transactional
    public Barbeiro cadastrarUsuarioBarbeiro(@Valid DadosCadastroBarbeiro dados) throws Exception {
        try {
            Usuario usuario = new Usuario();
            usuario.setNome(dados.nome());
            usuario.setEmail(dados.email());
            usuario.setSenha(passwordEncoder.encode(dados.senha()));
            usuario.setRole(Roles.BARBEIRO);

            Barbeiro barbeiro = new Barbeiro();
            barbeiro.setCpf(dados.cpf());
            barbeiro.setTelefone(dados.cpf());
            barbeiro.setUsuario(usuario);
            barbeiro.setStatus(Status.ATIVO);

            usuariosRepository.save(usuario);
            barbeiroRepository.save(barbeiro);

            return barbeiro;
        } catch (Exception e) {
            throw new Exception("Dados faltantes ou incorretos!");
        }
    }

    @Transactional
    public Barbeiro mudarBarbeiro(@Valid DadosAlteracaoBarbeiro dados) throws Exception {
        try {
            var barbeiro = barbeiroRepository.findById(dados.id()).orElseThrow(() -> new UsernameNotFoundException("Barbeiro não encontrado"));

            var usuario = usuariosRepository.getReferenceById(barbeiro.getUsuario().getId());

            if(dados.nome() != null){
                usuario.setNome(dados.nome());
            }

            if(dados.email() != null){
                usuario.setEmail(dados.email());
            }

            if(dados.telefone() != null){
                barbeiro.setTelefone(dados.telefone());
            }

            if(dados.cpf() != null){
                barbeiro.setCpf(dados.cpf());
            }

            barbeiroRepository.save(barbeiro);
            usuariosRepository.save(usuario);
            return barbeiro;
        } catch (Exception e) {
            throw new Exception("Dados incorretos - Não foi possível alterar o usuário");
        }
    }

    @Transactional
    public void desativarBarbeiro(Long id) {
        var barbeiro = barbeiroRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Barbeiro não encontrado"));

        barbeiro.setStatus(Status.DESATIVO);
        barbeiroRepository.save(barbeiro);
    }
}
