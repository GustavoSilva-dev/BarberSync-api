package com.barbersync.barbersync_api.Usuarios.repository;

import com.barbersync.barbersync_api.Usuarios.classes.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    UserDetails findByUsuarioEmail(String email);
}
