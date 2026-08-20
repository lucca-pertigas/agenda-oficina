package com.oficina.agenda.service.agenda;

import com.oficina.agenda.dto.AgendamentoDiaResponse;
import com.oficina.agenda.model.Agendamento;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AgendaDiaService {

    public List<AgendamentoDiaResponse> montar(
            LocalDate data,
            List<Agendamento> agendamentos) {

        List<AgendamentoDiaResponse> resultado =
                new ArrayList<>();

        for (Agendamento agendamento : agendamentos) {

            adicionarTrecho(
                    resultado,
                    agendamento,
                    LocalDateTime.of(
                            data,
                            LocalTime.of(8, 0)
                    ),
                    LocalDateTime.of(
                            data,
                            LocalTime.of(12, 0)
                    )
            );

            adicionarTrecho(
                    resultado,
                    agendamento,
                    LocalDateTime.of(
                            data,
                            LocalTime.of(13, 0)
                    ),
                    LocalDateTime.of(
                            data,
                            LocalTime.of(17, 0)
                    )
            );
        }

        return resultado;
    }


    private void adicionarTrecho(
            List<AgendamentoDiaResponse> resultado,
            Agendamento agendamento,
            LocalDateTime inicioPeriodo,
            LocalDateTime fimPeriodo) {

        LocalDateTime inicioExibicao =
                agendamento.getDataHoraInicio()
                        .isAfter(inicioPeriodo)
                        ? agendamento.getDataHoraInicio()
                        : inicioPeriodo;

        LocalDateTime fimExibicao =
                agendamento.getDataHoraFim()
                        .isBefore(fimPeriodo)
                        ? agendamento.getDataHoraFim()
                        : fimPeriodo;

        if (!inicioExibicao.isBefore(fimExibicao)) {
            return;
        }

        long duracao =
                Duration.between(
                        inicioExibicao,
                        fimExibicao
                ).toMinutes();

        AgendamentoDiaResponse trecho =
                new AgendamentoDiaResponse();

        trecho.setId(
                agendamento.getId()
        );

        trecho.setNomeCliente(
                agendamento.getNomeCliente()
        );

        trecho.setPlacaVeiculo(
                agendamento.getPlacaVeiculo()
        );

        trecho.setModeloVeiculoId(
                agendamento
                        .getModeloVeiculo()
                        .getId()
        );

        trecho.setModeloVeiculoNome(
                agendamento
                        .getModeloVeiculo()
                        .getNome()
        );

        trecho.setTecnicoId(
                agendamento
                        .getTecnico()
                        .getId()
        );

        trecho.setTecnicoNome(
                agendamento
                        .getTecnico()
                        .getNome()
        );

        trecho.setElevadorId(
                agendamento
                        .getElevador()
                        .getId()
        );

        trecho.setElevadorNumero(
                agendamento
                        .getElevador()
                        .getNumero()
        );

        List<Long> servicosIds =
                agendamento
                        .getServicos()
                        .stream()
                        .map(item ->
                                item
                                        .getServico()
                                        .getId()
                        )
                        .toList();

        List<String> servicosNomes =
                agendamento
                        .getServicos()
                        .stream()
                        .map(item ->
                                item
                                        .getServico()
                                        .getNome()
                        )
                        .toList();

        trecho.setServicosIds(
                servicosIds
        );

        trecho.setServicosNomes(
                servicosNomes
        );

        trecho.setServicosNomesTexto(
                String.join(
                        " + ",
                        servicosNomes
                )
        );

        trecho.setDataHoraInicioOriginal(
                agendamento.getDataHoraInicio()
        );

        trecho.setDataHoraFimOriginal(
                agendamento.getDataHoraFim()
        );

        trecho.setInicioExibicao(
                inicioExibicao
        );

        trecho.setFimExibicao(
                fimExibicao
        );

        trecho.setDuracaoExibicaoMinutos(
                (int) duracao
        );

        trecho.setStatus(
                agendamento.getStatus()
        );

        resultado.add(trecho);
    }
}