package com.barbersync.barbersync_api.Servicos.repository;

import com.barbersync.barbersync_api.Servicos.classes.Servico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServicoRepository extends JpaRepository<Servico, Long> {
    Page<Servico> findAllByAtivoTrue(Pageable paginacao);
    Optional<Servico> findByIdAndAtivoTrue(Long id);
}
