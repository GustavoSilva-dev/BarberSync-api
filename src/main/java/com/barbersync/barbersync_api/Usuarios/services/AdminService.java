package com.barbersync.barbersync_api.Usuarios.services;

import com.barbersync.barbersync_api.Usuarios.classes.Admin;
import com.barbersync.barbersync_api.Usuarios.classes.Usuario;
import com.barbersync.barbersync_api.Usuarios.dtos.DadosAlteracaoAdmin;
import com.barbersync.barbersync_api.Usuarios.dtos.DadosCadastroAdmin;
import com.barbersync.barbersync_api.Usuarios.dtos.Roles;
import com.barbersync.barbersync_api.Usuarios.dtos.Status;
import com.barbersync.barbersync_api.Usuarios.repository.AdminRepository;
import com.barbersync.barbersync_api.Usuarios.repository.UsuariosRepository;
import com.barbersync.barbersync_api.infra.exception.UsuarioNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Transactional
    public Admin cadastrarUsuarioAdmin(@Valid DadosCadastroAdmin dados) throws Exception {
        try {
            Usuario usuario = new Usuario();
            usuario.setNome(dados.nome());
            usuario.setEmail(dados.email());
            usuario.setSenha(passwordEncoder.encode(dados.senha()));
            usuario.setRole(Roles.ADMIN);

            Admin admin = new Admin();
            admin.setUsuario(usuario);
            admin.setAdminKey(dados.adminKey());
            admin.setStatus(Status.ATIVO);

            usuariosRepository.save(usuario);
            return adminRepository.save(admin);
        } catch (Exception e) {
            throw new Exception("Dados faltantes ou incorretos!");
        }

    }

    @Transactional
    public void alterarUsuarioAdmin(@Valid DadosAlteracaoAdmin dados) throws Exception {
        try {
            var admin = adminRepository.findById(dados.id()).orElseThrow(() -> new UsuarioNotFoundException("Admin não identificado no sistema"));

            var usuario = usuariosRepository.getReferenceById(admin.getUsuario().getId());

            if (dados.nome() != null){
                usuario.setNome(dados.nome());
            }

            if (dados.email() != null){
                usuario.setEmail(dados.email());
            }

            if (dados.senha() != null) {
                usuario.setSenha(passwordEncoder.encode(dados.senha()));
            }

            if (dados.adminKey() != 0){
                admin.setAdminKey(dados.adminKey());
            }
        } catch (Exception e) {
            throw new Exception("Erro em alteração de admin - Dados faltantes ou incorretos");
        }
    }

    @Transactional
    public void desativarAdmin(Long id) throws Exception {
        try {
            if (adminRepository.countAllByAtivo() > 0){
                var admin = adminRepository.findById(id).orElseThrow(() -> new UsuarioNotFoundException("Admin não identificado no sistema"));

                admin.setStatus(Status.DESATIVO);
            } else {
                throw new Exception("Impossível realizar exclusão: Deve haver no mínimo 1 ADMIN remanescente no sistema");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public boolean validarKey(@Valid int adminKey) {
        var admin = adminRepository.findByAdminKey(adminKey);

        if(admin != null){
            return true;
        }

        return false;
    }
}
