package com.oficina.agenda.dto;

import com.oficina.agenda.model.StatusAgendamento;

import java.time.LocalDateTime;

public class AgendamentoDiaResponse {

    private Long id;

    private Long tecnicoId;
    private String tecnicoNome;

    private Long elevadorId;
    private Integer elevadorNumero;

    private Long servicoId;
    private String servicoNome;

    private LocalDateTime dataHoraInicioOriginal;
    private LocalDateTime dataHoraFimOriginal;

    private LocalDateTime inicioExibicao;
    private LocalDateTime fimExibicao;

    private Integer duracaoExibicaoMinutos;

    private StatusAgendamento status;

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

    public LocalDateTime getDataHoraInicioOriginal() {
        return dataHoraInicioOriginal;
    }

    public void setDataHoraInicioOriginal(
            LocalDateTime dataHoraInicioOriginal) {

        this.dataHoraInicioOriginal = dataHoraInicioOriginal;
    }

    public LocalDateTime getDataHoraFimOriginal() {
        return dataHoraFimOriginal;
    }

    public void setDataHoraFimOriginal(
            LocalDateTime dataHoraFimOriginal) {

        this.dataHoraFimOriginal = dataHoraFimOriginal;
    }

    public LocalDateTime getInicioExibicao() {
        return inicioExibicao;
    }

    public void setInicioExibicao(
            LocalDateTime inicioExibicao) {

        this.inicioExibicao = inicioExibicao;
    }

    public LocalDateTime getFimExibicao() {
        return fimExibicao;
    }

    public void setFimExibicao(
            LocalDateTime fimExibicao) {

        this.fimExibicao = fimExibicao;
    }

    public Integer getDuracaoExibicaoMinutos() {
        return duracaoExibicaoMinutos;
    }

    public void setDuracaoExibicaoMinutos(
            Integer duracaoExibicaoMinutos) {

        this.duracaoExibicaoMinutos =
                duracaoExibicaoMinutos;
    }

    public StatusAgendamento getStatus() {
        return status;
    }

    public void setStatus(
            StatusAgendamento status) {

        this.status = status;
    }

    public Integer getMinutoInicio() {

        if (inicioExibicao == null) {
            return 0;
        }

        return inicioExibicao.getMinute();
    }
}