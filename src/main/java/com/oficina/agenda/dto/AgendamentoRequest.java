package com.oficina.agenda.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public class AgendamentoRequest {

    @NotNull(message = "Técnico é obrigatório")
    private Long tecnicoId;

    @NotNull(message = "Elevador é obrigatório")
    private Long elevadorId;

    @NotEmpty(
            message = "Selecione pelo menos um serviço"
    )
    private List<Long> servicosIds;

    @NotNull(
            message = "Data e hora de início são obrigatórias"
    )
    private LocalDateTime dataHoraInicio;


    public Long getTecnicoId() {
        return tecnicoId;
    }

    public void setTecnicoId(Long tecnicoId) {
        this.tecnicoId = tecnicoId;
    }


    public Long getElevadorId() {
        return elevadorId;
    }

    public void setElevadorId(Long elevadorId) {
        this.elevadorId = elevadorId;
    }


    public List<Long> getServicosIds() {
        return servicosIds;
    }

    public void setServicosIds(
            List<Long> servicosIds) {

        this.servicosIds = servicosIds;
    }


    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(
            LocalDateTime dataHoraInicio) {

        this.dataHoraInicio = dataHoraInicio;
    }
}