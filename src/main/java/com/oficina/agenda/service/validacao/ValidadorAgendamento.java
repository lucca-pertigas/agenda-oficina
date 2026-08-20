package com.oficina.agenda.service.validacao;

import com.oficina.agenda.model.Agendamento;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ValidadorAgendamento {

    private final List<RegraAgendamento> regras;


    public ValidadorAgendamento(
            List<RegraAgendamento> regras) {

        this.regras = regras;
    }


    public void validar(
            Agendamento agendamento,
            Long agendamentoId) {

        for (RegraAgendamento regra : regras) {

            regra.validar(
                    agendamento,
                    agendamentoId
            );
        }
    }
}