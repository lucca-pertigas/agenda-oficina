package com.oficina.agenda.service;

import com.oficina.agenda.dto.AgendamentoRequest;
import com.oficina.agenda.model.Agendamento;
import org.springframework.stereotype.Service;

@Service
public class AgendamentoResourceService {

    private final TecnicoService tecnicoService;
    private final ElevadorService elevadorService;
    private final ServicoService servicoService;

    public AgendamentoResourceService(
            TecnicoService tecnicoService,
            ElevadorService elevadorService,
            ServicoService servicoService) {

        this.tecnicoService = tecnicoService;
        this.elevadorService = elevadorService;
        this.servicoService = servicoService;
    }

    public void preencherRecursos(
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
}