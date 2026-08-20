package com.oficina.agenda.service;

import com.oficina.agenda.dto.AgendamentoResponse;
import com.oficina.agenda.notification.NotificadorAgendamento;
import com.oficina.agenda.service.evento.PublicadorEventoAgendamento;
import com.oficina.agenda.websocket.TipoEventoAgendamento;
import org.springframework.stereotype.Service;

@Service
public class AgendamentoEventoService
        implements PublicadorEventoAgendamento {

    private final NotificadorAgendamento notificadorAgendamento;


    public AgendamentoEventoService(
            NotificadorAgendamento notificadorAgendamento) {

        this.notificadorAgendamento =
                notificadorAgendamento;
    }


    @Override
    public void criado(
            AgendamentoResponse agendamento) {

        notificadorAgendamento.notificar(
                TipoEventoAgendamento.CRIADO,
                agendamento
        );
    }


    @Override
    public void atualizado(
            AgendamentoResponse agendamento) {

        notificadorAgendamento.notificar(
                TipoEventoAgendamento.ATUALIZADO,
                agendamento
        );
    }


    @Override
    public void cancelado(
            AgendamentoResponse agendamento) {

        notificadorAgendamento.notificar(
                TipoEventoAgendamento.CANCELADO,
                agendamento
        );
    }


    @Override
    public void concluido(
            AgendamentoResponse agendamento) {

        notificadorAgendamento.notificar(
                TipoEventoAgendamento.CONCLUIDO,
                agendamento
        );
    }
}