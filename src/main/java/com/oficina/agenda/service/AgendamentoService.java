package com.oficina.agenda.service;

import com.oficina.agenda.dto.AgendamentoRequest;
import com.oficina.agenda.dto.AgendamentoResponse;
import com.oficina.agenda.exception.ConflitoAgendamentoException;
import com.oficina.agenda.exception.RecursoNaoEncontradoException;
import com.oficina.agenda.exception.RegraNegocioException;
import com.oficina.agenda.mapper.AgendamentoMapper;
import com.oficina.agenda.model.Agendamento;
import com.oficina.agenda.model.StatusAgendamento;
import com.oficina.agenda.repository.AgendamentoRepository;
import com.oficina.agenda.websocket.AgendamentoWebSocketService;
import com.oficina.agenda.websocket.TipoEventoAgendamento;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final AgendamentoWebSocketService agendamentoWebSocketService;
    private final AgendamentoMapper agendamentoMapper;
    private final ConflitoAgendamentoService conflitoAgendamentoService;
    private final AgendamentoValidator agendamentoValidator;

    public AgendamentoService(
            AgendamentoRepository agendamentoRepository,
            AgendamentoWebSocketService agendamentoWebSocketService,
            AgendamentoMapper agendamentoMapper,
            ConflitoAgendamentoService conflitoAgendamentoService,
            AgendamentoValidator agendamentoValidator) {

        this.agendamentoRepository = agendamentoRepository;
        this.agendamentoWebSocketService = agendamentoWebSocketService;
        this.agendamentoMapper = agendamentoMapper;
        this.conflitoAgendamentoService = conflitoAgendamentoService;
        this.agendamentoValidator = agendamentoValidator;
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

        agendamentoMapper.preencherDados(
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

        agendamentoWebSocketService.enviarAtualizacao(
                TipoEventoAgendamento.CRIADO,
                response
        );

        return response;
    }

    public AgendamentoResponse atualizar(
            Long id,
            AgendamentoRequest request) {

        Agendamento agendamento = buscarPorId(id);

        validarPodeEditar(agendamento);

        agendamentoMapper.preencherDados(
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

        agendamentoWebSocketService.enviarAtualizacao(
                TipoEventoAgendamento.ATUALIZADO,
                response
        );

        return response;
    }

    public AgendamentoResponse cancelar(Long id) {

        Agendamento agendamento = buscarPorId(id);

        if (agendamento.getStatus() == StatusAgendamento.CONCLUIDO) {
            throw new RegraNegocioException(
                    "Agendamento concluído não pode ser cancelado"
            );
        }

        if (agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            throw new RegraNegocioException(
                    "Agendamento já está cancelado"
            );
        }

        agendamento.setStatus(
                StatusAgendamento.CANCELADO
        );

        Agendamento salvo =
                agendamentoRepository.save(agendamento);

        AgendamentoResponse response =
                agendamentoMapper.paraResponse(salvo);

        agendamentoWebSocketService.enviarAtualizacao(
                TipoEventoAgendamento.CANCELADO,
                response
        );

        return response;
    }

    public AgendamentoResponse concluir(Long id) {

        Agendamento agendamento = buscarPorId(id);

        validarPodeEditar(agendamento);

        agendamento.setStatus(
                StatusAgendamento.CONCLUIDO
        );

        Agendamento salvo =
                agendamentoRepository.save(agendamento);

        AgendamentoResponse response =
                agendamentoMapper.paraResponse(salvo);

        agendamentoWebSocketService.enviarAtualizacao(
                TipoEventoAgendamento.CONCLUIDO,
                response
        );

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

    private void validarPodeEditar(
            Agendamento agendamento) {

        if (agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            throw new RegraNegocioException(
                    "Agendamento cancelado não pode ser editado"
            );
        }

        if (agendamento.getStatus() == StatusAgendamento.CONCLUIDO) {
            throw new RegraNegocioException(
                    "Agendamento concluído não pode ser editado"
            );
        }
    }
}