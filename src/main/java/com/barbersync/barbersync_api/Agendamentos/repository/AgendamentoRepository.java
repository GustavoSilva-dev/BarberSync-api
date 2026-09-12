package com.barbersync.barbersync_api.Agendamentos.repository;

import com.barbersync.barbersync_api.Agendamentos.classes.Agendamento;
import com.barbersync.barbersync_api.Agendamentos.dtos.DadosCadastroAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    @Query("SELECT a FROM Agendamento a WHERE a.dataHoraInicio = :horario")
    Object findAllByDataHoraInicioNotIsEqual(LocalDateTime horario);
}
