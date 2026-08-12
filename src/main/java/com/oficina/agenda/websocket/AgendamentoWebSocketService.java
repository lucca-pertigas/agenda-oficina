package com.oficina.agenda.websocket;

import com.oficina.agenda.dto.AgendamentoEvento;
import com.oficina.agenda.dto.AgendamentoResponse;
import com.oficina.agenda.notification.NotificadorAgendamento;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class AgendamentoWebSocketService implements NotificadorAgendamento{

    private final SimpMessagingTemplate messagingTemplate;

    public AgendamentoWebSocketService(
            SimpMessagingTemplate messagingTemplate) {

        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void notificar(
            TipoEventoAgendamento tipo,
            AgendamentoResponse agendamento) {

        AgendamentoEvento evento =
                new AgendamentoEvento(tipo, agendamento);

        messagingTemplate.convertAndSend(
                "/topic/agendamentos",
                evento
        );
    }
}