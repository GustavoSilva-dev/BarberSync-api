package com.barbersync.barbersync_api.Usuarios.repository;

import com.barbersync.barbersync_api.Usuarios.classes.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByUsuarioEmail(String email);

    @Query("SELECT c FROM Cliente c WHERE c.status = 'ATIVO' LIMIT 1")
    Cliente findByAtivo(Long id);

    @Query("SELECT c FROM Cliente c WHERE c.status = 'ATIVO'")
    Page<Cliente> findAllByAtivo(Pageable page);

    @Query("SELECT c FROM Cliente c WHERE c.usuario.email = :subject LIMIT 1")
    UserDetails findBySubject(String subject);
}
