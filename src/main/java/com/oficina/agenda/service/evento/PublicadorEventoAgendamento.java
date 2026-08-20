package com.oficina.agenda.service.evento;

import com.oficina.agenda.dto.AgendamentoResponse;

public interface PublicadorEventoAgendamento {

    void criado(
            AgendamentoResponse agendamento
    );

    void atualizado(
            AgendamentoResponse agendamento
    );

    void cancelado(
            AgendamentoResponse agendamento
    );

    void concluido(
            AgendamentoResponse agendamento
    );
}