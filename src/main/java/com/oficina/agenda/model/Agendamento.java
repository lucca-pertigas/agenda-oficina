package com.oficina.agenda.model;

import com.oficina.agenda.exception.RegraNegocioException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "agendamentos")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "tecnico_id",
            nullable = false
    )
    @NotNull(message = "Técnico é obrigatório")
    private Tecnico tecnico;

    @ManyToOne
    @JoinColumn(
            name = "elevador_id",
            nullable = false
    )
    @NotNull(message = "Elevador é obrigatório")
    private Elevador elevador;

    @OneToMany(
            mappedBy = "agendamento",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<AgendamentoServico> servicos =
            new ArrayList<>();

    @NotNull(
            message = "Data e hora de início são obrigatórias"
    )
    private LocalDateTime dataHoraInicio;

    private LocalDateTime dataHoraFim;

    @Enumerated(EnumType.STRING)
    private StatusAgendamento status =
            StatusAgendamento.AGENDADO;


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


    public List<AgendamentoServico> getServicos() {
        return servicos;
    }

    public void setServicos(
            List<AgendamentoServico> servicos) {

        this.servicos.clear();

        if (servicos == null) {
            return;
        }

        for (AgendamentoServico item : servicos) {

            item.setAgendamento(this);

            this.servicos.add(item);
        }
    }


    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(
            LocalDateTime dataHoraInicio) {

        this.dataHoraInicio = dataHoraInicio;
    }


    public LocalDateTime getDataHoraFim() {
        return dataHoraFim;
    }

    public void setDataHoraFim(
            LocalDateTime dataHoraFim) {

        this.dataHoraFim = dataHoraFim;
    }


    public StatusAgendamento getStatus() {
        return status;
    }

    public void setStatus(
            StatusAgendamento status) {

        this.status = status;
    }


    public void adicionarServico(
            Servico servico) {

        AgendamentoServico item =
                new AgendamentoServico();

        item.setAgendamento(this);
        item.setServico(servico);

        servicos.add(item);
    }


    public void limparServicos() {

        servicos.clear();
    }


    public int calcularDuracaoTotalMinutos() {

        return servicos
                .stream()
                .map(AgendamentoServico::getServico)
                .mapToInt(Servico::getDuracaoMinutos)
                .sum();
    }


    public void validarPodeEditar() {

        if (status == StatusAgendamento.CANCELADO) {

            throw new RegraNegocioException(
                    "Agendamento cancelado não pode ser editado"
            );
        }

        if (status == StatusAgendamento.CONCLUIDO) {

            throw new RegraNegocioException(
                    "Agendamento concluído não pode ser editado"
            );
        }
    }


    public void cancelar() {

        if (status == StatusAgendamento.CANCELADO) {

            throw new RegraNegocioException(
                    "Agendamento já está cancelado"
            );
        }

        status = StatusAgendamento.CANCELADO;
    }


    public void concluir() {

        validarPodeEditar();

        status = StatusAgendamento.CONCLUIDO;
    }
}