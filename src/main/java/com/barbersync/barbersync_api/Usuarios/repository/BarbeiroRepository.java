package com.barbersync.barbersync_api.Usuarios.repository;

import com.barbersync.barbersync_api.Usuarios.classes.Barbeiro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

public interface BarbeiroRepository extends JpaRepository<Barbeiro, Long> {

    @Query("SELECT b FROM Barbeiro b WHERE b.status = 'ATIVO'")
    Page<Barbeiro> findAllByAtivo(Pageable paginacao);

    UserDetails findByEmail(String subject);
}
