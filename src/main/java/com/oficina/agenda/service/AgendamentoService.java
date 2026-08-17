package com.oficina.agenda.service;

import com.oficina.agenda.dto.AgendamentoDiaResponse;
import com.oficina.agenda.dto.AgendamentoRequest;
import com.oficina.agenda.dto.AgendamentoResponse;
import com.oficina.agenda.exception.RecursoNaoEncontradoException;
import com.oficina.agenda.mapper.AgendamentoMapper;
import com.oficina.agenda.model.Agendamento;
import com.oficina.agenda.model.StatusAgendamento;
import com.oficina.agenda.repository.AgendamentoRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final AgendamentoMapper agendamentoMapper;
    private final ConflitoAgendamentoService conflitoAgendamentoService;
    private final AgendamentoValidator agendamentoValidator;
    private final AgendamentoResourceService agendamentoResourceService;
    private final AgendamentoEventoService agendamentoEventoService;

    public AgendamentoService(
            AgendamentoRepository agendamentoRepository,
            AgendamentoMapper agendamentoMapper,
            ConflitoAgendamentoService conflitoAgendamentoService,
            AgendamentoValidator agendamentoValidator,
            AgendamentoResourceService agendamentoResourceService,
            AgendamentoEventoService agendamentoEventoService) {

        this.agendamentoRepository = agendamentoRepository;
        this.agendamentoMapper = agendamentoMapper;
        this.conflitoAgendamentoService = conflitoAgendamentoService;
        this.agendamentoValidator = agendamentoValidator;
        this.agendamentoResourceService = agendamentoResourceService;
        this.agendamentoEventoService = agendamentoEventoService;
    }

    // =========================================================
    // LISTAR TODOS
    // =========================================================

    public List<AgendamentoResponse> listarTodos() {

        return agendamentoRepository
                .findAll()
                .stream()
                .map(agendamentoMapper::paraResponse)
                .toList();
    }

    // =========================================================
    // BUSCAR POR ID
    // =========================================================

    public Agendamento buscarPorId(Long id) {

        return agendamentoRepository
                .findById(id)
                .orElseThrow(
                        () -> new RecursoNaoEncontradoException(
                                "Agendamento não encontrado"
                        )
                );
    }

    public AgendamentoResponse buscarResponsePorId(Long id) {

        Agendamento agendamento =
                buscarPorId(id);

        return agendamentoMapper
                .paraResponse(agendamento);
    }

    // =========================================================
    // SALVAR
    // =========================================================

    public AgendamentoResponse salvar(
            AgendamentoRequest request) {

        Agendamento agendamento =
                new Agendamento();

        agendamentoResourceService
                .preencherRecursos(
                        agendamento,
                        request
                );

        /*
         * Aqui é calculado o horário final,
         * considerando:
         *
         * 08:00 - 12:00 trabalho
         * 12:00 - 13:00 almoço
         * 13:00 - 17:00 trabalho
         * após 17:00 continua no próximo dia
         */
        agendamentoValidator
                .preparar(agendamento);

        conflitoAgendamentoService
                .validar(
                        agendamento,
                        null
                );

        Agendamento salvo =
                agendamentoRepository
                        .save(agendamento);

        AgendamentoResponse response =
                agendamentoMapper
                        .paraResponse(salvo);

        agendamentoEventoService
                .criado(response);

        return response;
    }

    // =========================================================
    // ATUALIZAR
    // =========================================================

    public AgendamentoResponse atualizar(
            Long id,
            AgendamentoRequest request) {

        Agendamento agendamento =
                buscarPorId(id);

        agendamento.validarPodeEditar();

        agendamentoResourceService
                .preencherRecursos(
                        agendamento,
                        request
                );

        agendamentoValidator
                .preparar(agendamento);

        conflitoAgendamentoService
                .validar(
                        agendamento,
                        id
                );

        Agendamento salvo =
                agendamentoRepository
                        .save(agendamento);

        AgendamentoResponse response =
                agendamentoMapper
                        .paraResponse(salvo);

        agendamentoEventoService
                .atualizado(response);

        return response;
    }

    // =========================================================
    // CANCELAR
    // =========================================================

    public AgendamentoResponse cancelar(
            Long id) {

        Agendamento agendamento =
                buscarPorId(id);

        agendamento.cancelar();

        Agendamento salvo =
                agendamentoRepository
                        .save(agendamento);

        AgendamentoResponse response =
                agendamentoMapper
                        .paraResponse(salvo);

        agendamentoEventoService
                .cancelado(response);

        return response;
    }

    // =========================================================
    // CONCLUIR
    // =========================================================

    public AgendamentoResponse concluir(
            Long id) {

        Agendamento agendamento =
                buscarPorId(id);

        agendamento.concluir();

        Agendamento salvo =
                agendamentoRepository
                        .save(agendamento);

        AgendamentoResponse response =
                agendamentoMapper
                        .paraResponse(salvo);

        agendamentoEventoService
                .concluido(response);

        return response;
    }

    // =========================================================
    // LISTAR POR PERÍODO
    // =========================================================

    public List<AgendamentoResponse> listarPorPeriodo(
            LocalDateTime inicio,
            LocalDateTime fim) {

        return agendamentoRepository
                .findByStatusNotAndDataHoraInicioLessThanAndDataHoraFimGreaterThanOrderByDataHoraInicioAsc(
                        StatusAgendamento.CANCELADO,
                        fim,
                        inicio
                )
                .stream()
                .map(agendamentoMapper::paraResponse)
                .toList();
    }

    // =========================================================
    // LISTAR POR PERÍODO E ELEVADOR
    // =========================================================

    public List<AgendamentoResponse> listarPorPeriodoEElevador(
            Long elevadorId,
            LocalDateTime inicio,
            LocalDateTime fim) {

        return agendamentoRepository
                .findByStatusNotAndDataHoraInicioLessThanAndDataHoraFimGreaterThanOrderByDataHoraInicioAsc(
                        StatusAgendamento.CANCELADO,
                        fim,
                        inicio
                )
                .stream()
                .filter(
                        agendamento ->
                                agendamento
                                        .getElevador()
                                        .getId()
                                        .equals(elevadorId)
                )
                .map(agendamentoMapper::paraResponse)
                .toList();
    }

    // =========================================================
    // LISTAR CANCELADOS
    // =========================================================

    public List<AgendamentoResponse> listarCancelados() {

        return agendamentoRepository
                .findAll()
                .stream()
                .filter(
                        agendamento ->
                                agendamento.getStatus()
                                        == StatusAgendamento.CANCELADO
                )
                .map(agendamentoMapper::paraResponse)
                .toList();
    }

    // =========================================================
    // LISTAR AGENDADOS
    // =========================================================

    public List<AgendamentoResponse> listarAgendados() {

        return agendamentoRepository
                .findAll()
                .stream()
                .filter(
                        agendamento ->
                                agendamento.getStatus()
                                        == StatusAgendamento.AGENDADO
                )
                .map(agendamentoMapper::paraResponse)
                .toList();
    }

    // =========================================================
    // AGENDA VISUAL DO DIA
    // =========================================================

    public List<AgendamentoDiaResponse> listarParaAgenda(
            LocalDate data) {

        LocalDateTime inicioDia =
                data.atStartOfDay();

        LocalDateTime fimDia =
                data.plusDays(1)
                        .atStartOfDay();

        List<Agendamento> agendamentos =
                agendamentoRepository
                        .findByStatusNotAndDataHoraInicioLessThanAndDataHoraFimGreaterThanOrderByDataHoraInicioAsc(
                                StatusAgendamento.CANCELADO,
                                fimDia,
                                inicioDia
                        );

        List<AgendamentoDiaResponse> resultado =
                new ArrayList<>();


        for (Agendamento agendamento : agendamentos) {

            /*
             * MANHÃ
             *
             * 08:00 até 12:00
             */
            adicionarTrechoSeExistir(
                    resultado,
                    agendamento,

                    LocalDateTime.of(
                            data,
                            LocalTime.of(8, 0)
                    ),

                    LocalDateTime.of(
                            data,
                            LocalTime.of(12, 0)
                    )
            );


            /*
             * TARDE
             *
             * 13:00 até 17:00
             */
            adicionarTrechoSeExistir(
                    resultado,
                    agendamento,

                    LocalDateTime.of(
                            data,
                            LocalTime.of(13, 0)
                    ),

                    LocalDateTime.of(
                            data,
                            LocalTime.of(17, 0)
                    )
            );
        }

        return resultado;
    }

    // =========================================================
    // CRIAR TRECHO VISUAL
    // =========================================================

    private void adicionarTrechoSeExistir(
            List<AgendamentoDiaResponse> resultado,
            Agendamento agendamento,
            LocalDateTime inicioPeriodo,
            LocalDateTime fimPeriodo) {

        /*
         * Pega o maior horário de início.
         */
        LocalDateTime inicioExibicao =
                agendamento
                        .getDataHoraInicio()
                        .isAfter(inicioPeriodo)
                        ?
                        agendamento.getDataHoraInicio()
                        :
                        inicioPeriodo;


        /*
         * Pega o menor horário de fim.
         */
        LocalDateTime fimExibicao =
                agendamento
                        .getDataHoraFim()
                        .isBefore(fimPeriodo)
                        ?
                        agendamento.getDataHoraFim()
                        :
                        fimPeriodo;


        /*
         * Se não houver interseção,
         * não cria card.
         */
        if (!inicioExibicao.isBefore(
                fimExibicao)) {

            return;
        }


        long duracao =
                Duration
                        .between(
                                inicioExibicao,
                                fimExibicao
                        )
                        .toMinutes();


        AgendamentoDiaResponse trecho =
                new AgendamentoDiaResponse();


        trecho.setId(
                agendamento.getId()
        );


        trecho.setTecnicoId(
                agendamento
                        .getTecnico()
                        .getId()
        );


        trecho.setTecnicoNome(
                agendamento
                        .getTecnico()
                        .getNome()
        );


        trecho.setElevadorId(
                agendamento
                        .getElevador()
                        .getId()
        );


        trecho.setElevadorNumero(
                agendamento
                        .getElevador()
                        .getNumero()
        );


        trecho.setServicoId(
                agendamento
                        .getServico()
                        .getId()
        );


        trecho.setServicoNome(
                agendamento
                        .getServico()
                        .getNome()
        );


        trecho.setDataHoraInicioOriginal(
                agendamento
                        .getDataHoraInicio()
        );


        trecho.setDataHoraFimOriginal(
                agendamento
                        .getDataHoraFim()
        );


        trecho.setInicioExibicao(
                inicioExibicao
        );


        trecho.setFimExibicao(
                fimExibicao
        );


        trecho.setDuracaoExibicaoMinutos(
                (int) duracao
        );


        trecho.setStatus(
                agendamento.getStatus()
        );


        resultado.add(trecho);
    }
}