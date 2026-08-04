package com.barbersync.barbersync_api.Usuarios.services;

import com.barbersync.barbersync_api.Usuarios.classes.Cliente;
import com.barbersync.barbersync_api.Usuarios.classes.Usuario;
import com.barbersync.barbersync_api.Usuarios.dtos.DadosAlteracaoCliente;
import com.barbersync.barbersync_api.Usuarios.dtos.DadosCadastroCliente;
import com.barbersync.barbersync_api.Usuarios.dtos.Roles;
import com.barbersync.barbersync_api.Usuarios.repository.ClienteRepository;
import com.barbersync.barbersync_api.Usuarios.repository.UsuariosRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class ClienteService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional
    public Cliente cadastrarUsuarioCliente(DadosCadastroCliente dados) throws Exception {
        try {
            Usuario usuario = new Usuario();

            usuario.setNome(dados.nome());
            usuario.setEmail(dados.email());
            usuario.setSenha(passwordEncoder.encode(dados.senha()));
            usuario.setRole(Roles.CLIENTE);

            Cliente cliente = new Cliente();

            if (Objects.equals(dados.telefone(), "")) {
                cliente.setTelefone("");
            } else {
                cliente.setTelefone(dados.telefone());
            }
            cliente.setUsuario(usuario);

            usuariosRepository.save(usuario);
            clienteRepository.save(cliente);

            return cliente;
        } catch (Exception e) {
            throw new Exception("Dados faltantes ou incorretos!");
        }
    }

    public Cliente alterarUsuarioCliente(@Valid DadosAlteracaoCliente dados) throws Exception {
        try {
            var cliente = clienteRepository.getReferenceById(dados.id());
            var usuario = usuariosRepository.getReferenceById(cliente.getUsuario().getId());

            if(dados.nome() != null){
                usuario.setNome(dados.nome());
            }

            if(dados.email() != null){
                usuario.setEmail(dados.email());
            }

            if(dados.senha() != null){
                usuario.setSenha(passwordEncoder.encode(dados.senha()));
            }

            if(dados.telefone() != null){
                cliente.setTelefone(dados.telefone());
            }

            usuariosRepository.save(usuario);
            clienteRepository.save(cliente);
            return cliente;
        } catch (Exception e) {
            throw new Exception("Dados incorretos - não foi possível alterar o usuário");
        }
    }
}
