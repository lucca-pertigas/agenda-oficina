package com.oficina.agenda.dto;

public class AgendamentoEvento {

    private String tipo;
    private AgendamentoResponse agendamento;

    public AgendamentoEvento() {
    }

    public AgendamentoEvento(
            String tipo,
            AgendamentoResponse agendamento) {

        this.tipo = tipo;
        this.agendamento = agendamento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public AgendamentoResponse getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(AgendamentoResponse agendamento) {
        this.agendamento = agendamento;
    }
}