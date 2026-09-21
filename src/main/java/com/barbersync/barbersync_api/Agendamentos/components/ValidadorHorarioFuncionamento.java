package com.barbersync.barbersync_api.Agendamentos.components;

import com.barbersync.barbersync_api.Agendamentos.dtos.DadosCadastroAgendamento;
import com.barbersync.barbersync_api.Servicos.repository.ServicoRepository;
import com.barbersync.barbersync_api.infra.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class ValidadorHorarioFuncionamento implements ValidadorAgendamento{

    private final LocalTime horarioInicioFuncionamento = LocalTime.of(8, 0);
    private final LocalTime horarioFinalFuncionamento = LocalTime.of(21, 0);

    @Autowired
    private ServicoRepository servicoRepository;

    @Override
    public void validarAgendamento(DadosCadastroAgendamento dados) {
        var servico = servicoRepository.getReferenceById(dados.servicoId());
        LocalDateTime dataFinal = dados.dataHoraInicio().plusMinutes(servico.getDuracaoEmMinutos());

        boolean horarioInicioInvalido = dados.dataHoraInicio().toLocalTime().isBefore(horarioInicioFuncionamento);
        boolean horarioFinalInvalido = dataFinal.toLocalTime().isAfter(horarioFinalFuncionamento);
        boolean diaInvalido = dados.dataHoraInicio().getDayOfWeek() == DayOfWeek.SUNDAY;

        if(horarioInicioInvalido || horarioFinalInvalido || diaInvalido) throw new ValidacaoException("HORÁRIO INVÁLIDO: A barbearia só funciona entre 08:00 e 21:00, de segunda à sábado.");
    }
}
