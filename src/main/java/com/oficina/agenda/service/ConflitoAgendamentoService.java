package com.oficina.agenda.service;

import com.oficina.agenda.exception.ConflitoAgendamentoException;
import com.oficina.agenda.model.Agendamento;
import com.oficina.agenda.model.StatusAgendamento;
import com.oficina.agenda.repository.AgendamentoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConflitoAgendamentoService {

    private static final LocalTime ABERTURA =
            LocalTime.of(8, 0);

    private static final LocalTime INICIO_ALMOCO =
            LocalTime.of(12, 0);

    private static final LocalTime FIM_ALMOCO =
            LocalTime.of(13, 0);

    private static final LocalTime FECHAMENTO =
            LocalTime.of(17, 0);


    private final AgendamentoRepository agendamentoRepository;


    public ConflitoAgendamentoService(
            AgendamentoRepository agendamentoRepository) {

        this.agendamentoRepository =
                agendamentoRepository;
    }


    public void validar(
            Agendamento novoAgendamento,
            Long idIgnorado) {

        /*
         * Primeiro buscamos todos os agendamentos
         * que podem cruzar o intervalo geral.
         *
         * Depois fazemos a validação real considerando
         * somente os períodos produtivos.
         */
        List<Agendamento> candidatos =
                agendamentoRepository
                        .findByStatusNotAndDataHoraInicioLessThanAndDataHoraFimGreaterThanOrderByDataHoraInicioAsc(
                                StatusAgendamento.CANCELADO,
                                novoAgendamento.getDataHoraFim(),
                                novoAgendamento.getDataHoraInicio()
                        );


        for (Agendamento existente : candidatos) {

            /*
             * Na edição, ignora o próprio agendamento.
             */
            if (
                    idIgnorado != null
                            &&
                            existente.getId().equals(idIgnorado)
            ) {
                continue;
            }


            boolean mesmoElevador =
                    existente
                            .getElevador()
                            .getId()
                            .equals(
                                    novoAgendamento
                                            .getElevador()
                                            .getId()
                            );


            boolean mesmoTecnico =
                    existente
                            .getTecnico()
                            .getId()
                            .equals(
                                    novoAgendamento
                                            .getTecnico()
                                            .getId()
                            );


            /*
             * Se não compartilha nem técnico
             * nem elevador, não precisamos comparar.
             */
            if (!mesmoElevador && !mesmoTecnico) {
                continue;
            }


            boolean existeConflito =
                    existeConflitoProdutivo(
                            novoAgendamento,
                            existente
                    );


            if (!existeConflito) {
                continue;
            }


            if (mesmoElevador) {

                throw new ConflitoAgendamentoException(
                        "Elevador já possui agendamento neste horário"
                );
            }


            if (mesmoTecnico) {

                throw new ConflitoAgendamentoException(
                        "Técnico já possui agendamento neste horário"
                );
            }
        }
    }


    /*
     * Verifica se dois agendamentos realmente
     * se sobrepõem durante horário produtivo.
     */
    private boolean existeConflitoProdutivo(
            Agendamento primeiro,
            Agendamento segundo) {

        List<PeriodoTrabalho> periodosPrimeiro =
                criarPeriodosProdutivos(
                        primeiro.getDataHoraInicio(),
                        primeiro.getDataHoraFim()
                );


        List<PeriodoTrabalho> periodosSegundo =
                criarPeriodosProdutivos(
                        segundo.getDataHoraInicio(),
                        segundo.getDataHoraFim()
                );


        for (PeriodoTrabalho periodoPrimeiro : periodosPrimeiro) {

            for (PeriodoTrabalho periodoSegundo : periodosSegundo) {

                if (
                        periodosSeSobrepoem(
                                periodoPrimeiro,
                                periodoSegundo
                        )
                ) {

                    return true;
                }
            }
        }


        return false;
    }


    /*
     * Divide um agendamento em períodos úteis.
     *
     * Exemplo:
     *
     * 11:00 até 14:00
     *
     * vira:
     *
     * 11:00 até 12:00
     * 13:00 até 14:00
     *
     * O almoço não entra.
     */
    private List<PeriodoTrabalho> criarPeriodosProdutivos(
            LocalDateTime inicio,
            LocalDateTime fim) {

        List<PeriodoTrabalho> periodos =
                new ArrayList<>();


        LocalDate data =
                inicio.toLocalDate();

        LocalDate dataFinal =
                fim.toLocalDate();


        while (!data.isAfter(dataFinal)) {

            /*
             * ============================
             * MANHÃ
             * 08:00 → 12:00
             * ============================
             */

            LocalDateTime inicioManha =
                    LocalDateTime.of(
                            data,
                            ABERTURA
                    );


            LocalDateTime fimManha =
                    LocalDateTime.of(
                            data,
                            INICIO_ALMOCO
                    );


            adicionarIntersecao(
                    periodos,
                    inicio,
                    fim,
                    inicioManha,
                    fimManha
            );


            /*
             * ============================
             * TARDE
             * 13:00 → 17:00
             * ============================
             */

            LocalDateTime inicioTarde =
                    LocalDateTime.of(
                            data,
                            FIM_ALMOCO
                    );


            LocalDateTime fimTarde =
                    LocalDateTime.of(
                            data,
                            FECHAMENTO
                    );


            adicionarIntersecao(
                    periodos,
                    inicio,
                    fim,
                    inicioTarde,
                    fimTarde
            );


            data =
                    data.plusDays(1);
        }


        return periodos;
    }


    /*
     * Adiciona somente a parte do agendamento
     * que realmente cruza o período de trabalho.
     */
    private void adicionarIntersecao(
            List<PeriodoTrabalho> periodos,
            LocalDateTime inicioAgendamento,
            LocalDateTime fimAgendamento,
            LocalDateTime inicioPeriodo,
            LocalDateTime fimPeriodo) {

        LocalDateTime inicioReal =
                inicioAgendamento.isAfter(inicioPeriodo)
                        ? inicioAgendamento
                        : inicioPeriodo;


        LocalDateTime fimReal =
                fimAgendamento.isBefore(fimPeriodo)
                        ? fimAgendamento
                        : fimPeriodo;


        if (inicioReal.isBefore(fimReal)) {

            periodos.add(
                    new PeriodoTrabalho(
                            inicioReal,
                            fimReal
                    )
            );
        }
    }


    /*
     * A sobreposição existe quando:
     *
     * inicio A < fim B
     * E
     * fim A > inicio B
     */
    private boolean periodosSeSobrepoem(
            PeriodoTrabalho primeiro,
            PeriodoTrabalho segundo) {

        return
                primeiro.inicio()
                        .isBefore(segundo.fim())
                        &&
                        primeiro.fim()
                                .isAfter(segundo.inicio());
    }


    /*
     * Objeto simples usado internamente
     * para representar um período produtivo.
     */
    private record PeriodoTrabalho(
            LocalDateTime inicio,
            LocalDateTime fim) {
    }
}