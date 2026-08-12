package com.oficina.agenda.websocket;

import com.oficina.agenda.dto.AgendamentoEvento;
import com.oficina.agenda.dto.AgendamentoResponse;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class AgendamentoWebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public AgendamentoWebSocketService(
            SimpMessagingTemplate messagingTemplate) {

        this.messagingTemplate = messagingTemplate;
    }

    public void enviarAtualizacao(
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