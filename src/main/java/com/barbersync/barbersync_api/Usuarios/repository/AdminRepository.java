package com.barbersync.barbersync_api.Usuarios.repository;

import com.barbersync.barbersync_api.Usuarios.classes.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    UserDetails findByEmail(String subject);
}
