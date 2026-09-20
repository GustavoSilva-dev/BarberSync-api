package com.barbersync.barbersync_api.infra.security;

import com.barbersync.barbersync_api.Usuarios.repository.AdminRepository;
import com.barbersync.barbersync_api.Usuarios.repository.BarbeiroRepository;
import com.barbersync.barbersync_api.Usuarios.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private BarbeiroRepository barbeiroRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var admin = adminRepository.findBySubject(email);
        if (admin != null) return admin;

        var cliente = clienteRepository.findBySubject(email);
        if (cliente != null) return cliente;

        var barbeiro = barbeiroRepository.findBySubject(email);
        if (barbeiro != null) return barbeiro;

        throw new UsernameNotFoundException("Usuário não encontrado: " + email);
    }
}
