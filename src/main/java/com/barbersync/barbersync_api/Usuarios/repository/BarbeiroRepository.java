package com.barbersync.barbersync_api.Usuarios.repository;

import com.barbersync.barbersync_api.Usuarios.classes.Barbeiro;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface BarbeiroRepository extends JpaRepository<Barbeiro, Long> {

    @Query("SELECT b FROM Barbeiro b WHERE b.status = 'ATIVO'")
    Page<Barbeiro> findAllByAtivo(Pageable paginacao);

    Optional<Barbeiro> findByUsuarioEmail(String email);

    @Query("SELECT b FROM Barbeiro b WHERE b.usuario.email = :subject LIMIT 1")
    UserDetails findBySubject(String subject);

    @Query("SELECT b FROM Barbeiro b WHERE b.status = 'ATIVO' LIMIT 1")
    Barbeiro findByAtivo(@NotNull(message = "Associe o barbeiro ao agendamento") Long aLong);
}
