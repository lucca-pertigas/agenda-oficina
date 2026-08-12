package com.oficina.agenda.mapper;

import com.oficina.agenda.dto.AgendamentoRequest;
import com.oficina.agenda.dto.AgendamentoResponse;
import com.oficina.agenda.model.Agendamento;
import com.oficina.agenda.service.ElevadorService;
import com.oficina.agenda.service.ServicoService;
import com.oficina.agenda.service.TecnicoService;
import org.springframework.stereotype.Component;

@Component
public class AgendamentoMapper {

    private final TecnicoService tecnicoService;
    private final ElevadorService elevadorService;
    private final ServicoService servicoService;

    public AgendamentoMapper(
            TecnicoService tecnicoService,
            ElevadorService elevadorService,
            ServicoService servicoService) {

        this.tecnicoService = tecnicoService;
        this.elevadorService = elevadorService;
        this.servicoService = servicoService;
    }

    public void preencherDados(
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

    public AgendamentoResponse paraResponse(
            Agendamento agendamento) {

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
}