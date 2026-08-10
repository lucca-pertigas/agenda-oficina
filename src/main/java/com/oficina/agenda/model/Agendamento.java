package com.oficina.agenda.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "agendamentos")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tecnico_id", nullable = false)
    @NotNull(message = "Técnico é obrigatório")
    private Tecnico tecnico;

    @ManyToOne
    @JoinColumn(name = "elevador_id", nullable = false)
    @NotNull(message = "Elevador é obrigatório")
    private Elevador elevador;

    @ManyToOne
    @JoinColumn(name = "servico_id", nullable = false)
    @NotNull(message = "Serviço é obrigatório")
    private Servico servico;

    @NotNull(message = "Data e hora de início são obrigatórias")
    private LocalDateTime dataHoraInicio;

    private LocalDateTime dataHoraFim;

    @Enumerated(EnumType.STRING)
    private StatusAgendamento status = StatusAgendamento.AGENDADO;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tecnico getTecnico() {
        return tecnico;
    }

    public void setTecnico(Tecnico tecnico) {
        this.tecnico = tecnico;
    }

    public Elevador getElevador() {
        return elevador;
    }

    public void setElevador(Elevador elevador) {
        this.elevador = elevador;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public LocalDateTime getDataHoraFim() {
        return dataHoraFim;
    }

    public void setDataHoraFim(LocalDateTime dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }

    public StatusAgendamento getStatus() {
        return status;
    }

    public void setStatus(StatusAgendamento status) {
        this.status = status;
    }
}
