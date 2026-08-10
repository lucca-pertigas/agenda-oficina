package com.oficina.agenda.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class AgendamentoRequest {

    @NotNull(message = "ID do técnico é obrigatório")
    private Long tecnicoId;

    @NotNull(message = "ID do elevador é obrigatório")
    private Long elevadorId;

    @NotNull(message = "ID do serviço é obrigatório")
    private Long servicoId;

    @NotNull(message = "Data e hora de início são obrigatórias")
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

    public Long getServicoId() {
        return servicoId;
    }

    public void setServicoId(Long servicoId) {
        this.servicoId = servicoId;
    }

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }
}
