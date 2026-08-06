package com.barbersync.barbersync_api.Usuarios.services;

import com.barbersync.barbersync_api.Usuarios.classes.Admin;
import com.barbersync.barbersync_api.Usuarios.classes.Usuario;
import com.barbersync.barbersync_api.Usuarios.dtos.DadosAlteracaoAdmin;
import com.barbersync.barbersync_api.Usuarios.dtos.DadosCadastroAdmin;
import com.barbersync.barbersync_api.Usuarios.dtos.Roles;
import com.barbersync.barbersync_api.Usuarios.repository.AdminRepository;
import com.barbersync.barbersync_api.Usuarios.repository.UsuariosRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void cadastrarUsuarioAdmin(@Valid DadosCadastroAdmin dados) throws Exception {
        try {
            Usuario usuario = new Usuario();
            usuario.setNome(dados.nome());
            usuario.setEmail(dados.email());
            usuario.setSenha(passwordEncoder.encode(dados.senha()));
            usuario.setRole(Roles.ADMIN);

            Admin admin = new Admin();
            admin.setUsuario(usuario);
            admin.setAdminKey(dados.adminKey());

            usuariosRepository.save(usuario);
            adminRepository.save(admin);
        } catch (Exception e) {
            throw new Exception("Dados faltantes ou incorretos!");
        }

    }

    public void alterarUsuarioAdmin(@Valid DadosAlteracaoAdmin dados) throws Exception {
        try {
            var admin = adminRepository.getReferenceById(dados.id());
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

    public void desativarAdmin(Long id) {
        var admin = adminRepository.findById(id);
    }
}
