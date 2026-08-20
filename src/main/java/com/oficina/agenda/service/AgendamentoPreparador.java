package com.oficina.agenda.service;

import com.oficina.agenda.exception.RegraNegocioException;
import com.oficina.agenda.model.Agendamento;
import org.springframework.stereotype.Component;

@Component
public class AgendamentoPreparador {

    private final HorarioFuncionamentoService horarioFuncionamentoService;


    public AgendamentoPreparador(
            HorarioFuncionamentoService horarioFuncionamentoService) {

        this.horarioFuncionamentoService =
                horarioFuncionamentoService;
    }


    public void preparar(
            Agendamento agendamento) {

        if (agendamento.getServicos() == null
                || agendamento.getServicos().isEmpty()) {

            throw new RegraNegocioException(
                    "Selecione pelo menos um serviço"
            );
        }


        int duracaoTotal =
                agendamento
                        .calcularDuracaoTotalMinutos();


        agendamento.setDataHoraFim(
                horarioFuncionamentoService.calcularFim(
                        agendamento.getDataHoraInicio(),
                        duracaoTotal
                )
        );
    }
}