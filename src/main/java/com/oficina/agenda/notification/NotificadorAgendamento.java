package com.oficina.agenda.notification;

import com.oficina.agenda.dto.AgendamentoResponse;
import com.oficina.agenda.websocket.TipoEventoAgendamento;

public interface NotificadorAgendamento {

    void notificar(
            TipoEventoAgendamento tipo,
            AgendamentoResponse agendamento
    );
}