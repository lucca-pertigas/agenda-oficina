package com.oficina.agenda.service.validacao;

import com.oficina.agenda.model.Agendamento;

public interface RegraAgendamento {

    void validar(
            Agendamento agendamento,
            Long agendamentoId
    );
}