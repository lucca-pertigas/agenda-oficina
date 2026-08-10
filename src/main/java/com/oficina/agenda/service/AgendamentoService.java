package com.oficina.agenda.service;

import com.oficina.agenda.model.Agendamento;
import com.oficina.agenda.model.StatusAgendamento;
import com.oficina.agenda.repository.AgendamentoRepository;
import com.oficina.agenda.exception.RegraNegocioException;
import com.oficina.agenda.exception.ConflitoAgendamentoException;
import com.oficina.agenda.exception.RecursoNaoEncontradoException;
import com.oficina.agenda.dto.AgendamentoRequest;
import com.oficina.agenda.dto.AgendamentoResponse;
import org.springframework.stereotype.Service;
import com.oficina.agenda.websocket.AgendamentoWebSocketService;

import java.util.List;
import java.time.LocalDateTime;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final TecnicoService tecnicoService;
    private final ElevadorService elevadorService;
    private final ServicoService servicoService;
    private final AgendamentoWebSocketService agendamentoWebSocketService;

    public AgendamentoService(
            AgendamentoRepository agendamentoRepository,
            TecnicoService tecnicoService,
            ElevadorService elevadorService,
            ServicoService servicoService,
            AgendamentoWebSocketService agendamentoWebSocketService) {

        this.agendamentoRepository = agendamentoRepository;
        this.tecnicoService = tecnicoService;
        this.elevadorService = elevadorService;
        this.servicoService = servicoService;
        this.agendamentoWebSocketService = agendamentoWebSocketService;
    }

    public List<AgendamentoResponse> listarTodos() {

        return agendamentoRepository.findAll()
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    public Agendamento buscarPorId(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Agendamento não encontrado"
                ));
    }

    public AgendamentoResponse salvar(AgendamentoRequest request) {

        Agendamento agendamento = new Agendamento();

        preencherDadosDoRequest(agendamento, request);

        prepararAgendamento(agendamento);

        validarConflitos(agendamento, null);

        Agendamento salvo = agendamentoRepository.save(agendamento);

        AgendamentoResponse response = converterParaResponse(salvo);

        agendamentoWebSocketService.enviarAtualizacao(response);

        return response;
    }

    public AgendamentoResponse atualizar(
            Long id,
            AgendamentoRequest request) {

        Agendamento agendamento = buscarPorId(id);

        validarPodeEditar(agendamento);

        preencherDadosDoRequest(agendamento, request);

        prepararAgendamento(agendamento);

        validarConflitos(agendamento, id);

        Agendamento salvo = agendamentoRepository.save(agendamento);

        AgendamentoResponse response = converterParaResponse(salvo);

        agendamentoWebSocketService.enviarAtualizacao(response);

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

        agendamento.setStatus(StatusAgendamento.CANCELADO);

        Agendamento salvo = agendamentoRepository.save(agendamento);

        AgendamentoResponse response = converterParaResponse(salvo);

        agendamentoWebSocketService.enviarAtualizacao(response);

        return response;
    }

    private void prepararAgendamento(Agendamento agendamento) {

        var tecnico = agendamento.getTecnico();
        var elevador = agendamento.getElevador();
        var servico = agendamento.getServico();

        if (!tecnico.getAtivo()) {
            throw new RegraNegocioException(
                    "Técnico está inativo"
            );
        }

        if (!elevador.getAtivo()) {
            throw new RegraNegocioException(
                    "Elevador está inativo"
            );
        }

        if (!servico.getAtivo()) {
            throw new RegraNegocioException(
                    "Serviço está inativo"
            );
        }

        agendamento.setDataHoraFim(
                agendamento.getDataHoraInicio()
                        .plusMinutes(servico.getDuracaoMinutos())
        );
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
                .map(this::converterParaResponse)
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
                .map(this::converterParaResponse)
                .toList();
    }

    public List<AgendamentoResponse> listarCancelados() {

        return agendamentoRepository
                .findByStatusOrderByDataHoraInicioAsc(
                        StatusAgendamento.CANCELADO
                )
                .stream()
                .map(this::converterParaResponse)
                .toList();
    }

    public AgendamentoResponse concluir(Long id) {

        Agendamento agendamento = buscarPorId(id);

        validarPodeEditar(agendamento);

        agendamento.setStatus(StatusAgendamento.CONCLUIDO);

        Agendamento salvo = agendamentoRepository.save(agendamento);

        AgendamentoResponse response = converterParaResponse(salvo);

        agendamentoWebSocketService.enviarAtualizacao(response);

        return response;
    }

    private void validarConflitos(Agendamento agendamento, Long idIgnorado) {

        boolean conflitoElevador;

        boolean conflitoTecnico;

        if (idIgnorado == null) {

            conflitoElevador =
                    agendamentoRepository
                            .existsByElevadorIdAndStatusNotAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(
                                    agendamento.getElevador().getId(),
                                    StatusAgendamento.CANCELADO,
                                    agendamento.getDataHoraFim(),
                                    agendamento.getDataHoraInicio()
                            );

            conflitoTecnico =
                    agendamentoRepository
                            .existsByTecnicoIdAndStatusNotAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(
                                    agendamento.getTecnico().getId(),
                                    StatusAgendamento.CANCELADO,
                                    agendamento.getDataHoraFim(),
                                    agendamento.getDataHoraInicio()
                            );

        } else {

            conflitoElevador =
                    agendamentoRepository
                            .existsByElevadorIdAndIdNotAndStatusNotAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(
                                    agendamento.getElevador().getId(),
                                    idIgnorado,
                                    StatusAgendamento.CANCELADO,
                                    agendamento.getDataHoraFim(),
                                    agendamento.getDataHoraInicio()
                            );

            conflitoTecnico =
                    agendamentoRepository
                            .existsByTecnicoIdAndIdNotAndStatusNotAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(
                                    agendamento.getTecnico().getId(),
                                    idIgnorado,
                                    StatusAgendamento.CANCELADO,
                                    agendamento.getDataHoraFim(),
                                    agendamento.getDataHoraInicio()
                            );
        }

        if (conflitoElevador) {
            throw new ConflitoAgendamentoException(
                    "Elevador já possui agendamento neste horário"
            );
        }

        if (conflitoTecnico) {
            throw new ConflitoAgendamentoException(
                    "Técnico já possui agendamento neste horário"
            );
        }
    }

    private void validarPodeEditar(Agendamento agendamento) {

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

    private void preencherDadosDoRequest(
            Agendamento agendamento,
            AgendamentoRequest request) {

        agendamento.setTecnico(
                tecnicoService.buscarPorId(request.getTecnicoId())
        );

        agendamento.setElevador(
                elevadorService.buscarPorId(request.getElevadorId())
        );

        agendamento.setServico(
                servicoService.buscarPorId(request.getServicoId())
        );

        agendamento.setDataHoraInicio(
                request.getDataHoraInicio()
        );
    }

    private AgendamentoResponse converterParaResponse(Agendamento agendamento) {

        AgendamentoResponse response = new AgendamentoResponse();

        response.setId(agendamento.getId());

        response.setTecnicoId(
                agendamento.getTecnico().getId()
        );

        response.setTecnicoNome(
                agendamento.getTecnico().getNome()
        );

        response.setElevadorId(
                agendamento.getElevador().getId()
        );

        response.setElevadorNumero(
                agendamento.getElevador().getNumero()
        );

        response.setServicoId(
                agendamento.getServico().getId()
        );

        response.setServicoNome(
                agendamento.getServico().getNome()
        );

        response.setDataHoraInicio(
                agendamento.getDataHoraInicio()
        );

        response.setDataHoraFim(
                agendamento.getDataHoraFim()
        );

        response.setStatus(
                agendamento.getStatus()
        );

        return response;
    }

    public AgendamentoResponse buscarResponsePorId(Long id) {

        Agendamento agendamento = buscarPorId(id);

        return converterParaResponse(agendamento);
    }


}