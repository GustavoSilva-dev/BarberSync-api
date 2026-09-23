package com.barbersync.barbersync_api.Usuarios.classes;

import com.barbersync.barbersync_api.Usuarios.dtos.DadosCadastroAdmin;
import com.barbersync.barbersync_api.Usuarios.dtos.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.management.relation.Role;

@Entity
@Table(name = "usuarios")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String senha;

    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    private Roles role;

    public boolean isCliente() {
        return this.role == Roles.CLIENTE;
    }

    public boolean isBarbeiro() {
        return this.role == Roles.BARBEIRO;
    }

    public boolean isAdmin() {
        return this.role == Roles.ADMIN;
    }
}
