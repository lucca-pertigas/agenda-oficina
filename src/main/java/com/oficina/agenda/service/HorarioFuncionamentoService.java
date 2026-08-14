package com.oficina.agenda.service;

import com.oficina.agenda.exception.RegraNegocioException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class HorarioFuncionamentoService {

    private static final LocalTime HORARIO_ABERTURA =
            LocalTime.of(8, 0);

    private static final LocalTime HORARIO_FECHAMENTO =
            LocalTime.of(17, 0);

    public LocalDateTime calcularFim(
            LocalDateTime inicio,
            int duracaoMinutos) {

        validarInicio(inicio);

        LocalDateTime momentoAtual = inicio;

        int minutosRestantes = duracaoMinutos;

        while (minutosRestantes > 0) {

            LocalDateTime fechamentoDia =
                    LocalDateTime.of(
                            momentoAtual.toLocalDate(),
                            HORARIO_FECHAMENTO
                    );

            long minutosDisponiveis =
                    java.time.Duration
                            .between(
                                    momentoAtual,
                                    fechamentoDia
                            )
                            .toMinutes();

            if (minutosRestantes <= minutosDisponiveis) {

                return momentoAtual.plusMinutes(
                        minutosRestantes
                );
            }

            minutosRestantes -=
                    (int) minutosDisponiveis;

            LocalDate proximoDia =
                    momentoAtual
                            .toLocalDate()
                            .plusDays(1);

            momentoAtual =
                    LocalDateTime.of(
                            proximoDia,
                            HORARIO_ABERTURA
                    );
        }

        return momentoAtual;
    }

    private void validarInicio(
            LocalDateTime inicio) {

        LocalTime horario =
                inicio.toLocalTime();

        if (horario.isBefore(HORARIO_ABERTURA)) {

            throw new RegraNegocioException(
                    "A oficina abre às 08:00"
            );
        }

        if (!horario.isBefore(HORARIO_FECHAMENTO)) {

            throw new RegraNegocioException(
                    "Não é possível iniciar um serviço às 17:00 ou depois"
            );
        }
    }

    public LocalTime getHorarioAbertura() {
        return HORARIO_ABERTURA;
    }

    public LocalTime getHorarioFechamento() {
        return HORARIO_FECHAMENTO;
    }
}