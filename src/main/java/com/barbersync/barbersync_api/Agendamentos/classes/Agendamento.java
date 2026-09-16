package com.barbersync.barbersync_api.Agendamentos.classes;

import com.barbersync.barbersync_api.Agendamentos.dtos.StatusAgendamento;
import com.barbersync.barbersync_api.Servicos.classes.Servico;
import com.barbersync.barbersync_api.Usuarios.classes.Barbeiro;
import com.barbersync.barbersync_api.Usuarios.classes.Cliente;
import com.barbersync.barbersync_api.Usuarios.dtos.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Table(name = "agendamentos")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_agendamento")
    private Long id;

    @Column(name = "data_hora_inicio")
    private LocalDateTime dataHoraInicio;

    @Column(name = "data_hora_final")
    private LocalDateTime dataHoraFinal;

    @Column(name = "status_agendamento")
    @Enumerated(EnumType.STRING)
    private StatusAgendamento statusAgendamento;

    @ManyToOne
    @JoinColumn(name = "id_barbeiro")
    private Barbeiro barbeiro;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_servico")
    private Servico servico;

}
