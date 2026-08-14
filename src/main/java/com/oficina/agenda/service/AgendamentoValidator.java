package com.oficina.agenda.service;

import com.oficina.agenda.model.Agendamento;
import org.springframework.stereotype.Component;

@Component
public class AgendamentoValidator {

    private final HorarioFuncionamentoService horarioFuncionamentoService;

    public AgendamentoValidator(
            HorarioFuncionamentoService horarioFuncionamentoService) {

        this.horarioFuncionamentoService =
                horarioFuncionamentoService;
    }

    public void preparar(
            Agendamento agendamento) {

        Integer duracaoMinutos =
                agendamento
                        .getServico()
                        .getDuracaoMinutos();

        agendamento.setDataHoraFim(
                horarioFuncionamentoService.calcularFim(
                        agendamento.getDataHoraInicio(),
                        duracaoMinutos
                )
        );
    }
}