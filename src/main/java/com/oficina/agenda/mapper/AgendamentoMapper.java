package com.oficina.agenda.mapper;

import com.oficina.agenda.dto.AgendamentoResponse;
import com.oficina.agenda.model.Agendamento;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AgendamentoMapper {

    public AgendamentoResponse paraResponse(
            Agendamento agendamento) {

        AgendamentoResponse response =
                new AgendamentoResponse();


        response.setId(
                agendamento.getId()
        );


        response.setTecnicoId(
                agendamento
                        .getTecnico()
                        .getId()
        );


        response.setTecnicoNome(
                agendamento
                        .getTecnico()
                        .getNome()
        );


        response.setElevadorId(
                agendamento
                        .getElevador()
                        .getId()
        );


        response.setElevadorNumero(
                agendamento
                        .getElevador()
                        .getNumero()
        );


        List<Long> servicosIds =
                agendamento
                        .getServicos()
                        .stream()
                        .map(item ->
                                item
                                        .getServico()
                                        .getId()
                        )
                        .toList();


        List<String> servicosNomes =
                agendamento
                        .getServicos()
                        .stream()
                        .map(item ->
                                item
                                        .getServico()
                                        .getNome()
                        )
                        .toList();


        response.setServicosIds(
                servicosIds
        );


        response.setServicosNomes(
                servicosNomes
        );


        response.setServicosNomesTexto(
                String.join(
                        " + ",
                        servicosNomes
                )
        );


        response.setDataHoraInicio(
                agendamento.getDataHoraInicio()
        );


        response.setDataHoraFim(
                agendamento.getDataHoraFim()
        );


        response.setDuracaoMinutos(
                agendamento
                        .calcularDuracaoTotalMinutos()
        );


        response.setStatus(
                agendamento.getStatus()
        );


        return response;
    }
}