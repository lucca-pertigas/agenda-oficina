package com.oficina.agenda.dto;

import com.oficina.agenda.model.StatusAgendamento;

import java.time.LocalDateTime;
import java.util.List;

public class AgendamentoResponse {

    private Long id;

    private Long tecnicoId;
    private String tecnicoNome;

    private Long elevadorId;
    private Integer elevadorNumero;

    private List<Long> servicosIds;
    private List<String> servicosNomes;

    private String servicosNomesTexto;

    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;

    private Integer duracaoMinutos;

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

    public void setTecnicoNome(
            String tecnicoNome) {

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

    public void setElevadorNumero(
            Integer elevadorNumero) {

        this.elevadorNumero = elevadorNumero;
    }


    public List<Long> getServicosIds() {
        return servicosIds;
    }

    public void setServicosIds(
            List<Long> servicosIds) {

        this.servicosIds = servicosIds;
    }


    public List<String> getServicosNomes() {
        return servicosNomes;
    }

    public void setServicosNomes(
            List<String> servicosNomes) {

        this.servicosNomes = servicosNomes;
    }


    public String getServicosNomesTexto() {
        return servicosNomesTexto;
    }

    public void setServicosNomesTexto(
            String servicosNomesTexto) {

        this.servicosNomesTexto =
                servicosNomesTexto;
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


    public Integer getDuracaoMinutos() {
        return duracaoMinutos;
    }

    public void setDuracaoMinutos(
            Integer duracaoMinutos) {

        this.duracaoMinutos = duracaoMinutos;
    }


    public StatusAgendamento getStatus() {
        return status;
    }

    public void setStatus(
            StatusAgendamento status) {

        this.status = status;
    }


    public Integer getMinutoInicio() {

        if (dataHoraInicio == null) {
            return 0;
        }

        return dataHoraInicio.getMinute();
    }
}