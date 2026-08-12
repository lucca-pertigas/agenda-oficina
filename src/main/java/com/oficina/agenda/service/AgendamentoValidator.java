package com.oficina.agenda.service;

import com.oficina.agenda.exception.RegraNegocioException;
import com.oficina.agenda.model.Agendamento;
import org.springframework.stereotype.Service;

@Service
public class AgendamentoValidator {

    public void preparar(Agendamento agendamento) {

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
}