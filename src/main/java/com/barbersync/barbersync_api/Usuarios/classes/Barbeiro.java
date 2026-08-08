package com.barbersync.barbersync_api.Usuarios.classes;

import com.barbersync.barbersync_api.Usuarios.dtos.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "barbeiros")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Barbeiro implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private String cpf;
    private String telefone;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.usuario.getRole()));
    }

    @Override
    public @Nullable String getPassword() {
        return this.usuario.getSenha();
    }

    @Override
    public String getUsername() {
        return this.usuario.getEmail();
    }

    @Override
    public boolean isEnabled() {
        return this.status == Status.ATIVO;
    }
}
