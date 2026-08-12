package com.oficina.agenda.mapper;

import com.oficina.agenda.dto.AgendamentoResponse;
import com.oficina.agenda.model.Agendamento;
import org.springframework.stereotype.Component;

@Component
public class AgendamentoMapper {

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