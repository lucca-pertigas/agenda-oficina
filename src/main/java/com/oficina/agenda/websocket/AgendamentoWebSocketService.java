package com.oficina.agenda.websocket;

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

    public void enviarAtualizacao(AgendamentoResponse agendamento) {

        messagingTemplate.convertAndSend(
                "/topic/agendamentos",
                agendamento
        );
    }
}