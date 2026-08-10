package com.oficina.agenda.service;

import com.oficina.agenda.model.Agendamento;
import com.oficina.agenda.model.StatusAgendamento;
import com.oficina.agenda.repository.AgendamentoRepository;
import com.oficina.agenda.exception.RegraNegocioException;
import com.oficina.agenda.exception.ConflitoAgendamentoException;
import com.oficina.agenda.exception.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDateTime;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final TecnicoService tecnicoService;
    private final ElevadorService elevadorService;
    private final ServicoService servicoService;

    public AgendamentoService(
            AgendamentoRepository agendamentoRepository,
            TecnicoService tecnicoService,
            ElevadorService elevadorService,
            ServicoService servicoService) {

        this.agendamentoRepository = agendamentoRepository;
        this.tecnicoService = tecnicoService;
        this.elevadorService = elevadorService;
        this.servicoService = servicoService;
    }

    public List<Agendamento> listarTodos() {
        return agendamentoRepository.findAll();
    }

    public Agendamento buscarPorId(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Agendamento não encontrado"
                ));
    }

    public Agendamento salvar(Agendamento agendamento) {

        prepararAgendamento(agendamento);

        validarConflitos(agendamento, null);

        return agendamentoRepository.save(agendamento);
    }

    public Agendamento atualizar(
            Long id,
            Agendamento agendamentoAtualizado) {

        Agendamento agendamento = buscarPorId(id);

        validarPodeEditar(agendamento);

        agendamento.setTecnico(
                agendamentoAtualizado.getTecnico()
        );

        agendamento.setElevador(
                agendamentoAtualizado.getElevador()
        );

        agendamento.setServico(
                agendamentoAtualizado.getServico()
        );

        agendamento.setDataHoraInicio(
                agendamentoAtualizado.getDataHoraInicio()
        );

        prepararAgendamento(agendamento);

        validarConflitos(agendamento, id);

        return agendamentoRepository.save(agendamento);
    }

    public Agendamento cancelar(Long id) {

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

        return agendamentoRepository.save(agendamento);
    }

    private void prepararAgendamento(Agendamento agendamento) {

        if (agendamento.getTecnico().getId() == null) {
            throw new RegraNegocioException("ID do técnico é obrigatório");
        }

        if (agendamento.getElevador().getId() == null) {
            throw new RegraNegocioException("ID do elevador é obrigatório");
        }

        if (agendamento.getServico().getId() == null) {
            throw new RegraNegocioException("ID do serviço é obrigatório");
        }

        var tecnico = tecnicoService.buscarPorId(
                agendamento.getTecnico().getId()
        );

        var elevador = elevadorService.buscarPorId(
                agendamento.getElevador().getId()
        );

        var servico = servicoService.buscarPorId(
                agendamento.getServico().getId()
        );

        if (!tecnico.getAtivo()) {
            throw new RegraNegocioException("Técnico está inativo");
        }

        if (!elevador.getAtivo()) {
            throw new RegraNegocioException("Elevador está inativo");
        }

        if (!servico.getAtivo()) {
            throw new RegraNegocioException("Serviço está inativo");
        }

        agendamento.setTecnico(tecnico);
        agendamento.setElevador(elevador);
        agendamento.setServico(servico);

        agendamento.setDataHoraFim(
                agendamento.getDataHoraInicio()
                        .plusMinutes(servico.getDuracaoMinutos())
        );
    }

    public List<Agendamento> listarPorPeriodo(
            LocalDateTime inicio,
            LocalDateTime fim) {

        return agendamentoRepository
                .findByStatusNotAndDataHoraInicioBetweenOrderByDataHoraInicioAsc(
                        StatusAgendamento.CANCELADO,
                        inicio,
                        fim
                );
    }

    public List<Agendamento> listarPorPeriodoEElevador(
            Long elevadorId,
            LocalDateTime inicio,
            LocalDateTime fim) {

        return agendamentoRepository
                .findByElevadorIdAndStatusNotAndDataHoraInicioBetweenOrderByDataHoraInicioAsc(
                        elevadorId,
                        StatusAgendamento.CANCELADO,
                        inicio,
                        fim
                );
    }

    public List<Agendamento> listarCancelados() {

        return agendamentoRepository
                .findByStatusOrderByDataHoraInicioAsc(
                        StatusAgendamento.CANCELADO
                );
    }

    public Agendamento concluir(Long id) {
        Agendamento agendamento = buscarPorId(id);

        validarPodeEditar(agendamento);

        agendamento.setStatus(StatusAgendamento.CONCLUIDO);

        return agendamentoRepository.save(agendamento);
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

}