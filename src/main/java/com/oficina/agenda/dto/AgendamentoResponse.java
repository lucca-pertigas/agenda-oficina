package com.oficina.agenda.dto;

import com.oficina.agenda.model.StatusAgendamento;

import java.time.LocalDateTime;

public class AgendamentoResponse {

    private Long id;

    private Long tecnicoId;
    private String tecnicoNome;

    private Long elevadorId;
    private Integer elevadorNumero;

    private Long servicoId;
    private String servicoNome;

    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;

    private StatusAgendamento status;

    private Integer duracaoMinutos;
    private Integer minutoInicio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTecnicoId() {
        return tecnicoId;
    }

    public void setTecnicoId(Long tecnicoId) {
        this.tecnicoId = tecnicoId;
    }

    public String getTecnicoNome() {
        return tecnicoNome;
    }

    public void setTecnicoNome(String tecnicoNome) {
        this.tecnicoNome = tecnicoNome;
    }

    public Long getElevadorId() {
        return elevadorId;
    }

    public void setElevadorId(Long elevadorId) {
        this.elevadorId = elevadorId;
    }

    public Integer getElevadorNumero() {
        return elevadorNumero;
    }

    public void setElevadorNumero(Integer elevadorNumero) {
        this.elevadorNumero = elevadorNumero;
    }

    public Long getServicoId() {
        return servicoId;
    }

    public void setServicoId(Long servicoId) {
        this.servicoId = servicoId;
    }

    public String getServicoNome() {
        return servicoNome;
    }

    public void setServicoNome(String servicoNome) {
        this.servicoNome = servicoNome;
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

    public Integer getDuracaoMinutos() {
        return duracaoMinutos;
    }

    public void setDuracaoMinutos(Integer duracaoMinutos) {
        this.duracaoMinutos = duracaoMinutos;
    }

    public Integer getMinutoInicio() {
        return minutoInicio;
    }

    public void setMinutoInicio(Integer minutoInicio) {
        this.minutoInicio = minutoInicio;
    }
}