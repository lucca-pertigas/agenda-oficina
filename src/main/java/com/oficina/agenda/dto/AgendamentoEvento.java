package com.oficina.agenda.dto;

import com.oficina.agenda.websocket.TipoEventoAgendamento;

public class AgendamentoEvento {

    private TipoEventoAgendamento tipo;
    private AgendamentoResponse agendamento;

    public AgendamentoEvento() {
    }

    public AgendamentoEvento(
            TipoEventoAgendamento tipo,
            AgendamentoResponse agendamento) {

        this.tipo = tipo;
        this.agendamento = agendamento;
    }

    public TipoEventoAgendamento getTipo() {
        return tipo;
    }

    public void setTipo(TipoEventoAgendamento tipo) {
        this.tipo = tipo;
    }

    public AgendamentoResponse getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(AgendamentoResponse agendamento) {
        this.agendamento = agendamento;
    }
}