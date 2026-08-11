package com.barbersync.barbersync_api.Usuarios.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.barbersync.barbersync_api.Usuarios.classes.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    UserDetails findByUsuarioEmail(String subject);

    @Query("SELECT COUNT(a) FROM Admin a WHERE a.status = 'ATIVO'")
    Long countAllByAtivo();

    @Query("SELECT a FROM Admin a WHERE a.status = 'ATIVO'")
    Page<Admin> findAllbyAtivo(Pageable page);
}
