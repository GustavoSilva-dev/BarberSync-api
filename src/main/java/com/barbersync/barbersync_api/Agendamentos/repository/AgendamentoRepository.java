package com.barbersync.barbersync_api.Agendamentos.repository;

import com.barbersync.barbersync_api.Agendamentos.classes.Agendamento;
import com.barbersync.barbersync_api.Agendamentos.dtos.DadosCadastroAgendamento;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    @Query("SELECT a FROM Agendamento a WHERE a.dataHoraInicio = :horario")
    Object findAllByDataHoraInicioNotIsEqual(LocalDateTime horario);

    @Query("SELECT COUNT(a) > 0 FROM Agendamento a WHERE a.barbeiro.id = :barbeiroId AND a.statusAgendamento != 'CANCELADO' AND a.dataHoraInicio < :dataFinal AND a.dataHoraFinal > :dataInicio")
    Boolean findByAgendamentoBarbeiroConflict(Long barbeiroId, LocalDateTime dataInicio, LocalDateTime dataFinal);
}