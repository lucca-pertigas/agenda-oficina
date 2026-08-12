package com.oficina.agenda.service;

import com.oficina.agenda.exception.ConflitoAgendamentoException;
import com.oficina.agenda.model.Agendamento;
import com.oficina.agenda.model.StatusAgendamento;
import com.oficina.agenda.repository.AgendamentoRepository;
import org.springframework.stereotype.Service;

@Service
public class ConflitoAgendamentoService {

    private final AgendamentoRepository agendamentoRepository;

    public ConflitoAgendamentoService(
            AgendamentoRepository agendamentoRepository) {

        this.agendamentoRepository = agendamentoRepository;
    }

    public void validar(
            Agendamento agendamento,
            Long idIgnorado) {

        boolean conflitoElevador;
        boolean conflitoTecnico;

        if (idIgnorado == null) {

            conflitoElevador =
                    agendamentoRepository
                            .existsByElevadorIdAndStatusNotAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(
                                    agendamento.getElevador().getId(),
                                    StatusAgendamento.CANCELADO,
                                    agendamento.getDataHoraFim(),
                                    agendamento.getDataHoraInicio()
                            );

            conflitoTecnico =
                    agendamentoRepository
                            .existsByTecnicoIdAndStatusNotAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(
                                    agendamento.getTecnico().getId(),
                                    StatusAgendamento.CANCELADO,
                                    agendamento.getDataHoraFim(),
                                    agendamento.getDataHoraInicio()
                            );

        } else {

            conflitoElevador =
                    agendamentoRepository
                            .existsByElevadorIdAndIdNotAndStatusNotAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(
                                    agendamento.getElevador().getId(),
                                    idIgnorado,
                                    StatusAgendamento.CANCELADO,
                                    agendamento.getDataHoraFim(),
                                    agendamento.getDataHoraInicio()
                            );

            conflitoTecnico =
                    agendamentoRepository
                            .existsByTecnicoIdAndIdNotAndStatusNotAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(
                                    agendamento.getTecnico().getId(),
                                    idIgnorado,
                                    StatusAgendamento.CANCELADO,
                                    agendamento.getDataHoraFim(),
                                    agendamento.getDataHoraInicio()
                            );
        }

        if (conflitoElevador) {
            throw new ConflitoAgendamentoException(
                    "Elevador já possui agendamento neste horário"
            );
        }

        if (conflitoTecnico) {
            throw new ConflitoAgendamentoException(
                    "Técnico já possui agendamento neste horário"
            );
        }
    }
}