package com.oficina.agenda.service;

import com.oficina.agenda.dto.AgendamentoResponse;
import com.oficina.agenda.notification.NotificadorAgendamento;
import com.oficina.agenda.websocket.TipoEventoAgendamento;
import org.springframework.stereotype.Service;

@Service
public class AgendamentoEventoService {

    private final NotificadorAgendamento notificadorAgendamento;

    public AgendamentoEventoService(
            NotificadorAgendamento notificadorAgendamento) {

        this.notificadorAgendamento = notificadorAgendamento;
    }

    public void criado(AgendamentoResponse agendamento) {
        notificadorAgendamento.notificar(
                TipoEventoAgendamento.CRIADO,
                agendamento
        );
    }

    public void atualizado(AgendamentoResponse agendamento) {
        notificadorAgendamento.notificar(
                TipoEventoAgendamento.ATUALIZADO,
                agendamento
        );
    }

    public void cancelado(AgendamentoResponse agendamento) {
        notificadorAgendamento.notificar(
                TipoEventoAgendamento.CANCELADO,
                agendamento
        );
    }

    public void concluido(AgendamentoResponse agendamento) {
        notificadorAgendamento.notificar(
                TipoEventoAgendamento.CONCLUIDO,
                agendamento
        );
    }
}