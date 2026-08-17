package com.oficina.agenda.model;

import jakarta.persistence.*;

@Entity
@Table(name = "agendamento_servicos")
public class AgendamentoServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "agendamento_id",
            nullable = false
    )
    private Agendamento agendamento;

    @ManyToOne
    @JoinColumn(
            name = "servico_id",
            nullable = false
    )
    private Servico servico;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Agendamento getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(
            Agendamento agendamento) {

        this.agendamento = agendamento;
    }


    public Servico getServico() {
        return servico;
    }

    public void setServico(
            Servico servico) {

        this.servico = servico;
    }
}