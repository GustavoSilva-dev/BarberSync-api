package com.barbersync.barbersync_api.Usuarios.classes;

import com.barbersync.barbersync_api.Usuarios.dtos.StatusBarbeiro;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "barbeiros")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Barbeiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private String cpf;
    private String telefone;

    @Enumerated(EnumType.STRING)
    private StatusBarbeiro status;
}
