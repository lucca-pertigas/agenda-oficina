package com.oficina.agenda.service;

import com.oficina.agenda.dto.AgendamentoRequest;
import com.oficina.agenda.dto.AgendamentoResponse;
import com.oficina.agenda.exception.RecursoNaoEncontradoException;
import com.oficina.agenda.mapper.AgendamentoMapper;
import com.oficina.agenda.model.Agendamento;
import com.oficina.agenda.model.StatusAgendamento;
import com.oficina.agenda.repository.AgendamentoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public List<AgendamentoResponse> listarTodos() {

        return agendamentoRepository.findAll()
                .stream()
                .map(agendamentoMapper::paraResponse)
                .toList();
    }

    public Agendamento buscarPorId(Long id) {

        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Agendamento não encontrado"
                ));
    }

    public AgendamentoResponse buscarResponsePorId(Long id) {

        Agendamento agendamento = buscarPorId(id);

        return agendamentoMapper.paraResponse(agendamento);
    }

    public AgendamentoResponse salvar(AgendamentoRequest request) {

        Agendamento agendamento = new Agendamento();

        agendamentoResourceService.preencherRecursos(
                agendamento,
                request
        );

        agendamentoValidator.preparar(agendamento);

        conflitoAgendamentoService.validar(
                agendamento,
                null
        );

        Agendamento salvo =
                agendamentoRepository.save(agendamento);

        AgendamentoResponse response =
                agendamentoMapper.paraResponse(salvo);

        agendamentoEventoService.criado(response);

        return response;
    }

    public AgendamentoResponse atualizar(
            Long id,
            AgendamentoRequest request) {

        Agendamento agendamento = buscarPorId(id);

        agendamento.validarPodeEditar();

        agendamentoResourceService.preencherRecursos(
                agendamento,
                request
        );

        agendamentoValidator.preparar(agendamento);

        conflitoAgendamentoService.validar(
                agendamento,
                id
        );

        Agendamento salvo =
                agendamentoRepository.save(agendamento);

        AgendamentoResponse response =
                agendamentoMapper.paraResponse(salvo);

        agendamentoEventoService.atualizado(response);

        return response;
    }

    public AgendamentoResponse cancelar(Long id) {

        Agendamento agendamento = buscarPorId(id);

        agendamento.cancelar();

        Agendamento salvo =
                agendamentoRepository.save(agendamento);

        AgendamentoResponse response =
                agendamentoMapper.paraResponse(salvo);

        agendamentoEventoService.cancelado(response);

        return response;
    }

    public AgendamentoResponse concluir(Long id) {

        Agendamento agendamento = buscarPorId(id);

        agendamento.concluir();

        Agendamento salvo =
                agendamentoRepository.save(agendamento);

        AgendamentoResponse response =
                agendamentoMapper.paraResponse(salvo);

        agendamentoEventoService.concluido(response);

        return response;
    }

    public List<AgendamentoResponse> listarPorPeriodo(
            LocalDateTime inicio,
            LocalDateTime fim) {

        return agendamentoRepository
                .findByStatusNotAndDataHoraInicioBetweenOrderByDataHoraInicioAsc(
                        StatusAgendamento.CANCELADO,
                        inicio,
                        fim
                )
                .stream()
                .map(agendamentoMapper::paraResponse)
                .toList();
    }

    public List<AgendamentoResponse> listarPorPeriodoEElevador(
            Long elevadorId,
            LocalDateTime inicio,
            LocalDateTime fim) {

        return agendamentoRepository
                .findByElevadorIdAndStatusNotAndDataHoraInicioBetweenOrderByDataHoraInicioAsc(
                        elevadorId,
                        StatusAgendamento.CANCELADO,
                        inicio,
                        fim
                )
                .stream()
                .map(agendamentoMapper::paraResponse)
                .toList();
    }

    public List<AgendamentoResponse> listarCancelados() {

        return agendamentoRepository
                .findByStatusOrderByDataHoraInicioAsc(
                        StatusAgendamento.CANCELADO
                )
                .stream()
                .map(agendamentoMapper::paraResponse)
                .toList();
    }

    public List<AgendamentoResponse> listarAgendados() {

        return agendamentoRepository
                .findByStatusOrderByDataHoraInicioAsc(
                        StatusAgendamento.AGENDADO
                )
                .stream()
                .map(agendamentoMapper::paraResponse)
                .toList();
    }
}