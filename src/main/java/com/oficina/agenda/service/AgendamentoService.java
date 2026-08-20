package com.oficina.agenda.service;

import com.oficina.agenda.dto.AgendamentoDiaResponse;
import com.oficina.agenda.dto.AgendamentoRequest;
import com.oficina.agenda.dto.AgendamentoResponse;
import com.oficina.agenda.exception.RecursoNaoEncontradoException;
import com.oficina.agenda.mapper.AgendamentoMapper;
import com.oficina.agenda.model.Agendamento;
import com.oficina.agenda.model.StatusAgendamento;
import com.oficina.agenda.repository.AgendamentoRepository;
import com.oficina.agenda.service.agenda.AgendaDiaService;
import com.oficina.agenda.service.evento.PublicadorEventoAgendamento;
import com.oficina.agenda.service.validacao.ValidadorAgendamento;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;

    private final AgendamentoMapper agendamentoMapper;

    private final AgendamentoPreparador agendamentoPreparador;

    private final AgendamentoResourceService agendamentoResourceService;

    private final PublicadorEventoAgendamento publicadorEvento;

    private final AgendaDiaService agendaDiaService;

    private final ValidadorAgendamento validadorAgendamento;


    public AgendamentoService(
            AgendamentoRepository agendamentoRepository,
            AgendamentoMapper agendamentoMapper,
            AgendamentoPreparador agendamentoPreparador,
            AgendamentoResourceService agendamentoResourceService,
            PublicadorEventoAgendamento publicadorEvento,
            AgendaDiaService agendaDiaService,
            ValidadorAgendamento validadorAgendamento) {

        this.agendamentoRepository =
                agendamentoRepository;

        this.agendamentoMapper =
                agendamentoMapper;

        this.agendamentoPreparador =
                agendamentoPreparador;

        this.agendamentoResourceService =
                agendamentoResourceService;

        this.publicadorEvento =
                publicadorEvento;

        this.agendaDiaService =
                agendaDiaService;

        this.validadorAgendamento =
                validadorAgendamento;
    }


    // =========================================================
    // LISTAR TODOS
    // =========================================================

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarTodos() {

        return agendamentoRepository
                .findAll()
                .stream()
                .map(
                        agendamentoMapper::paraResponse
                )
                .toList();
    }


    // =========================================================
    // BUSCAR POR ID
    // =========================================================

    @Transactional(readOnly = true)
    public Agendamento buscarPorId(
            Long id) {

        return agendamentoRepository
                .findById(id)
                .orElseThrow(
                        () ->
                                new RecursoNaoEncontradoException(
                                        "Agendamento não encontrado"
                                )
                );
    }

    @Transactional(readOnly = true)
    public AgendamentoResponse buscarResponsePorId(
            Long id) {

        Agendamento agendamento =
                buscarPorId(id);

        return agendamentoMapper
                .paraResponse(
                        agendamento
                );
    }


    // =========================================================
    // SALVAR
    // =========================================================

    @Transactional
    public AgendamentoResponse salvar(
            AgendamentoRequest request) {

        Agendamento agendamento =
                new Agendamento();


        // =========================================
        // PREENCHE OS DADOS DO AGENDAMENTO
        // =========================================

        agendamentoResourceService
                .preencherRecursos(
                        agendamento,
                        request
                );


        // =========================================
        // PREPARA O AGENDAMENTO
        //
        // Exemplo:
        // - calcula duração
        // - calcula data/hora final
        // =========================================

        agendamentoPreparador
                .preparar(
                        agendamento
                );


        // =========================================
        // REGRAS DE NEGÓCIO
        //
        // Executa automaticamente todas as
        // implementações de RegraAgendamento.
        //
        // Exemplo:
        // - indisponibilidade do técnico
        // - conflito de técnico/elevador
        // =========================================

        validadorAgendamento
                .validar(
                        agendamento,
                        null
                );


        // =========================================
        // SALVA
        // =========================================

        Agendamento salvo =
                agendamentoRepository
                        .save(
                                agendamento
                        );


        // =========================================
        // MONTA RESPONSE
        // =========================================

        AgendamentoResponse response =
                agendamentoMapper
                        .paraResponse(
                                salvo
                        );


        // =========================================
        // PUBLICA EVENTO
        // =========================================

        publicadorEvento
                .criado(
                        response
                );


        return response;
    }


    // =========================================================
    // ATUALIZAR
    // =========================================================

    @Transactional
    public AgendamentoResponse atualizar(
            Long id,
            AgendamentoRequest request) {

        Agendamento agendamento =
                buscarPorId(id);


        // =========================================
        // VERIFICA SE PODE EDITAR
        // =========================================

        agendamento
                .validarPodeEditar();


        // =========================================
        // ATUALIZA DADOS
        // =========================================

        agendamentoResourceService
                .preencherRecursos(
                        agendamento,
                        request
                );


        // =========================================
        // RECALCULA O AGENDAMENTO
        // =========================================

        agendamentoPreparador
                .preparar(
                        agendamento
                );


        // =========================================
        // REGRAS DE NEGÓCIO
        //
        // O id é enviado para que regras de
        // conflito consigam ignorar o próprio
        // agendamento durante a edição.
        // =========================================

        validadorAgendamento
                .validar(
                        agendamento,
                        id
                );


        // =========================================
        // SALVA
        // =========================================

        Agendamento salvo =
                agendamentoRepository
                        .save(
                                agendamento
                        );


        // =========================================
        // RESPONSE
        // =========================================

        AgendamentoResponse response =
                agendamentoMapper
                        .paraResponse(
                                salvo
                        );


        // =========================================
        // EVENTO
        // =========================================

        publicadorEvento
                .atualizado(
                        response
                );


        return response;
    }


    // =========================================================
    // CANCELAR
    // =========================================================

    @Transactional
    public AgendamentoResponse cancelar(
            Long id) {

        Agendamento agendamento =
                buscarPorId(id);


        agendamento
                .cancelar();


        Agendamento salvo =
                agendamentoRepository
                        .save(
                                agendamento
                        );


        AgendamentoResponse response =
                agendamentoMapper
                        .paraResponse(
                                salvo
                        );


        publicadorEvento
                .cancelado(
                        response
                );


        return response;
    }


    // =========================================================
    // CONCLUIR
    // =========================================================

    @Transactional
    public AgendamentoResponse concluir(
            Long id) {

        Agendamento agendamento =
                buscarPorId(id);


        agendamento
                .concluir();


        Agendamento salvo =
                agendamentoRepository
                        .save(
                                agendamento
                        );


        AgendamentoResponse response =
                agendamentoMapper
                        .paraResponse(
                                salvo
                        );


        publicadorEvento
                .concluido(
                        response
                );


        return response;
    }


    // =========================================================
    // LISTAR POR PERÍODO
    // =========================================================

    @Transactional(readOnly = true)
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
                .map(
                        agendamentoMapper::paraResponse
                )
                .toList();
    }


    // =========================================================
    // LISTAR POR PERÍODO E ELEVADOR
    // =========================================================

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarPorPeriodoEElevador(
            Long elevadorId,
            LocalDateTime inicio,
            LocalDateTime fim) {

        return agendamentoRepository
                .findByStatusNotAndElevadorIdAndDataHoraInicioLessThanAndDataHoraFimGreaterThanOrderByDataHoraInicioAsc(
                        StatusAgendamento.CANCELADO,
                        elevadorId,
                        fim,
                        inicio
                )
                .stream()
                .map(
                        agendamentoMapper::paraResponse
                )
                .toList();
    }


    // =========================================================
    // LISTAR CANCELADOS
    // =========================================================

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarCancelados() {

        return agendamentoRepository
                .findByStatusOrderByDataHoraInicioAsc(
                        StatusAgendamento.CANCELADO
                )
                .stream()
                .map(
                        agendamentoMapper::paraResponse
                )
                .toList();
    }


    // =========================================================
    // LISTAR AGENDADOS
    // =========================================================

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarAgendados() {

        return agendamentoRepository
                .findByStatusOrderByDataHoraInicioAsc(
                        StatusAgendamento.AGENDADO
                )
                .stream()
                .map(
                        agendamentoMapper::paraResponse
                )
                .toList();
    }


    // =========================================================
    // AGENDA VISUAL DO DIA
    // =========================================================

    public List<AgendamentoDiaResponse> listarParaAgenda(
            LocalDate data) {

        LocalDateTime inicioDia =
                data
                        .atStartOfDay();


        LocalDateTime fimDia =
                data
                        .plusDays(1)
                        .atStartOfDay();


        List<Agendamento> agendamentos =
                agendamentoRepository
                        .findByStatusNotAndDataHoraInicioLessThanAndDataHoraFimGreaterThanOrderByDataHoraInicioAsc(
                                StatusAgendamento.CANCELADO,
                                fimDia,
                                inicioDia
                        );


        return agendaDiaService
                .montar(
                        data,
                        agendamentos
                );
    }
}