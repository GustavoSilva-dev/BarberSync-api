package com.barbersync.barbersync_api.Agendamentos.repository;

import com.barbersync.barbersync_api.Agendamentos.classes.Agendamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    @Query("SELECT COUNT(a) > 0 FROM Agendamento a WHERE a.barbeiro.id = :barbeiroId AND a.statusAgendamento != 'CANCELADO' AND a.dataHoraInicio < :dataFinal AND a.dataHoraFinal > :dataInicio")
    Boolean findByAgendamentoBarbeiroConflict(Long barbeiroId, LocalDateTime dataInicio, LocalDateTime dataFinal);

    @Query("SELECT COUNT(a) > 0 FROM Agendamento a WHERE a.cliente.id = :clienteId AND a.statusAgendamento != 'CANCELADO' AND a.dataHoraInicio < :dataFinal AND a.dataHoraFinal > :dataInicio")
    Boolean findByAgendamentoClienteConflict(Long clienteId, LocalDateTime dataInicio, LocalDateTime dataFinal);

    Page<Agendamento> findAll(Pageable pageable);
}