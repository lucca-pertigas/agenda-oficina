package com.oficina.agenda.service;

import com.oficina.agenda.exception.RegraNegocioException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class HorarioFuncionamentoService {

    private static final LocalTime HORARIO_ABERTURA =
            LocalTime.of(8, 0);

    private static final LocalTime INICIO_ALMOCO =
            LocalTime.of(12, 0);

    private static final LocalTime FIM_ALMOCO =
            LocalTime.of(13, 0);

    private static final LocalTime HORARIO_FECHAMENTO =
            LocalTime.of(17, 0);


    public LocalDateTime calcularFim(
            LocalDateTime inicio,
            int duracaoMinutos) {

        validarInicio(inicio);

        if (duracaoMinutos <= 0) {
            throw new RegraNegocioException(
                    "A duração do serviço deve ser maior que zero"
            );
        }

        LocalDateTime momentoAtual = inicio;

        int minutosRestantes = duracaoMinutos;


        while (minutosRestantes > 0) {

            LocalTime horarioAtual =
                    momentoAtual.toLocalTime();

            LocalDate dataAtual =
                    momentoAtual.toLocalDate();


            /*
             * MANHÃ
             * 08:00 até 12:00
             */
            if (horarioAtual.isBefore(INICIO_ALMOCO)) {

                LocalDateTime inicioAlmoco =
                        LocalDateTime.of(
                                dataAtual,
                                INICIO_ALMOCO
                        );

                long minutosDisponiveis =
                        Duration.between(
                                momentoAtual,
                                inicioAlmoco
                        ).toMinutes();


                if (minutosRestantes <= minutosDisponiveis) {

                    return momentoAtual.plusMinutes(
                            minutosRestantes
                    );
                }


                minutosRestantes -=
                        (int) minutosDisponiveis;


                /*
                 * Chegou às 12:00.
                 * Pula diretamente para 13:00.
                 */
                momentoAtual =
                        LocalDateTime.of(
                                dataAtual,
                                FIM_ALMOCO
                        );

                continue;
            }


            /*
             * HORÁRIO DE ALMOÇO
             * 12:00 até 13:00
             */
            if (horarioAtual.isBefore(FIM_ALMOCO)) {

                momentoAtual =
                        LocalDateTime.of(
                                dataAtual,
                                FIM_ALMOCO
                        );

                continue;
            }


            /*
             * TARDE
             * 13:00 até 17:00
             */
            if (horarioAtual.isBefore(HORARIO_FECHAMENTO)) {

                LocalDateTime fechamento =
                        LocalDateTime.of(
                                dataAtual,
                                HORARIO_FECHAMENTO
                        );

                long minutosDisponiveis =
                        Duration.between(
                                momentoAtual,
                                fechamento
                        ).toMinutes();


                if (minutosRestantes <= minutosDisponiveis) {

                    return momentoAtual.plusMinutes(
                            minutosRestantes
                    );
                }


                minutosRestantes -=
                        (int) minutosDisponiveis;


                /*
                 * Chegou às 17:00.
                 * Continua no próximo dia às 08:00.
                 */
                momentoAtual =
                        proximoDiaUtilizado(
                                dataAtual
                        );

                continue;
            }


            /*
             * Caso tenha chegado às 17:00.
             */
            momentoAtual =
                    proximoDiaUtilizado(
                            dataAtual
                    );
        }


        return momentoAtual;
    }


    private LocalDateTime proximoDiaUtilizado(
            LocalDate dataAtual) {

        LocalDate proximoDia =
                dataAtual.plusDays(1);

        return LocalDateTime.of(
                proximoDia,
                HORARIO_ABERTURA
        );
    }


    private void validarInicio(
            LocalDateTime inicio) {

        if (inicio == null) {
            throw new RegraNegocioException(
                    "Data e horário de início são obrigatórios"
            );
        }


        LocalTime horario =
                inicio.toLocalTime();


        if (horario.isBefore(HORARIO_ABERTURA)) {

            throw new RegraNegocioException(
                    "A oficina abre às 08:00"
            );
        }


        if (!horario.isBefore(HORARIO_FECHAMENTO)) {

            throw new RegraNegocioException(
                    "A oficina fecha às 17:00"
            );
        }


        /*
         * Bloqueia início de serviço durante o almoço.
         */
        if (!horario.isBefore(INICIO_ALMOCO)
                && horario.isBefore(FIM_ALMOCO)) {

            throw new RegraNegocioException(
                    "Não é possível iniciar um serviço entre 12:00 e 13:00"
            );
        }
    }


    public LocalTime getHorarioAbertura() {
        return HORARIO_ABERTURA;
    }


    public LocalTime getInicioAlmoco() {
        return INICIO_ALMOCO;
    }


    public LocalTime getFimAlmoco() {
        return FIM_ALMOCO;
    }


    public LocalTime getHorarioFechamento() {
        return HORARIO_FECHAMENTO;
    }
}