package com.oficina.agenda.service;

import com.oficina.agenda.model.Agendamento;
import com.oficina.agenda.model.StatusAgendamento;
import com.oficina.agenda.repository.AgendamentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Agendamento não encontrado"
                ));
    }

    public Agendamento salvar(Agendamento agendamento) {

        prepararAgendamento(agendamento);

        boolean conflitoElevador =
                agendamentoRepository
                        .existsByElevadorIdAndStatusNotAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(
                                agendamento.getElevador().getId(),
                                StatusAgendamento.CANCELADO,
                                agendamento.getDataHoraFim(),
                                agendamento.getDataHoraInicio()
                        );

        if (conflitoElevador) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Elevador já possui agendamento neste horário"
            );
        }

        boolean conflitoTecnico =
                agendamentoRepository
                        .existsByTecnicoIdAndStatusNotAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(
                                agendamento.getTecnico().getId(),
                                StatusAgendamento.CANCELADO,
                                agendamento.getDataHoraFim(),
                                agendamento.getDataHoraInicio()
                        );

        if (conflitoTecnico) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Técnico já possui agendamento neste horário"
            );
        }

        return agendamentoRepository.save(agendamento);
    }

    public Agendamento atualizar(
            Long id,
            Agendamento agendamentoAtualizado) {

        Agendamento agendamento = buscarPorId(id);

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

        boolean conflitoElevador =
                agendamentoRepository
                        .existsByElevadorIdAndIdNotAndStatusNotAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(
                                agendamento.getElevador().getId(),
                                id,
                                StatusAgendamento.CANCELADO,
                                agendamento.getDataHoraFim(),
                                agendamento.getDataHoraInicio()
                        );

        if (conflitoElevador) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Elevador já possui outro agendamento neste horário"
            );
        }

        boolean conflitoTecnico =
                agendamentoRepository
                        .existsByTecnicoIdAndIdNotAndStatusNotAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(
                                agendamento.getTecnico().getId(),
                                id,
                                StatusAgendamento.CANCELADO,
                                agendamento.getDataHoraFim(),
                                agendamento.getDataHoraInicio()
                        );

        if (conflitoTecnico) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Técnico já possui outro agendamento neste horário"
            );
        }

        return agendamentoRepository.save(agendamento);
    }

    public Agendamento cancelar(Long id) {

        Agendamento agendamento = buscarPorId(id);

        agendamento.setStatus(
                StatusAgendamento.CANCELADO
        );

        return agendamentoRepository.save(agendamento);
    }

    private void prepararAgendamento(
            Agendamento agendamento) {

        if (agendamento.getServico() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Serviço é obrigatório"
            );
        }

        if (agendamento.getTecnico() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Técnico é obrigatório"
            );
        }

        if (agendamento.getElevador() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Elevador é obrigatório"
            );
        }

        if (agendamento.getDataHoraInicio() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Data e hora de início são obrigatórias"
            );
        }

        if (agendamento.getTecnico().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "ID do técnico é obrigatório"
            );
        }

        if (agendamento.getElevador().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "ID do elevador é obrigatório"
            );
        }

        if (agendamento.getServico().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "ID do serviço é obrigatório"
            );
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
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Técnico está inativo"
            );
        }

        if (!elevador.getAtivo()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Elevador está inativo"
            );
        }

        if (!servico.getAtivo()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Serviço está inativo"
            );
        }

        agendamento.setTecnico(tecnico);
        agendamento.setElevador(elevador);
        agendamento.setServico(servico);

        agendamento.setDataHoraFim(
                agendamento.getDataHoraInicio()
                        .plusMinutes(
                                servico.getDuracaoMinutos()
                        )
        );
    }
}