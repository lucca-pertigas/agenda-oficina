package com.oficina.agenda.service.validacao;

import com.oficina.agenda.exception.RegraNegocioException;
import com.oficina.agenda.model.Agendamento;
import com.oficina.agenda.service.IndisponibilidadeTecnicoService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class RegraDisponibilidadeTecnico
        implements RegraAgendamento {

    private final IndisponibilidadeTecnicoService
            indisponibilidadeTecnicoService;


    public RegraDisponibilidadeTecnico(
            IndisponibilidadeTecnicoService
                    indisponibilidadeTecnicoService) {

        this.indisponibilidadeTecnicoService =
                indisponibilidadeTecnicoService;
    }


    @Override
    public void validar(
            Agendamento agendamento,
            Long agendamentoId) {

        Long tecnicoId =
                agendamento
                        .getTecnico()
                        .getId();


        LocalDate data =
                agendamento
                        .getDataHoraInicio()
                        .toLocalDate();


        boolean indisponivel =
                indisponibilidadeTecnicoService
                        .tecnicoIndisponivel(
                                tecnicoId,
                                data
                        );


        if (indisponivel) {

            throw new RegraNegocioException(
                    "O técnico está indisponível nesta data"
            );
        }
    }
}