package com.barbersync.barbersync_api.Usuarios.services;

import com.barbersync.barbersync_api.Usuarios.classes.Cliente;
import com.barbersync.barbersync_api.Usuarios.classes.Usuario;
import com.barbersync.barbersync_api.Usuarios.dtos.DadosCadastroCliente;
import com.barbersync.barbersync_api.Usuarios.dtos.Roles;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    @Transactional
    public void cadastrarUsuarioCliente(DadosCadastroCliente dados){
        Usuario usuario = new Usuario();

        usuario.setNome(dados.nome());
        usuario.setEmail(dados.email());
        usuario.setSenha(dados.senha());
        usuario.setRole(Roles.CLIENTE);

        Cliente cliente = new Cliente();

        cliente.setTelefone(dados.telefone());
        cliente.setUsuario(usuario);
    }
}
