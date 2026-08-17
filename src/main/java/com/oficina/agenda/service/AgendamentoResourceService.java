package com.oficina.agenda.service;

import com.oficina.agenda.dto.AgendamentoRequest;
import com.oficina.agenda.exception.RegraNegocioException;
import com.oficina.agenda.model.Agendamento;
import com.oficina.agenda.model.Elevador;
import com.oficina.agenda.model.Servico;
import com.oficina.agenda.model.Tecnico;
import org.springframework.stereotype.Service;

@Service
public class AgendamentoResourceService {

    private final TecnicoService tecnicoService;
    private final ElevadorService elevadorService;
    private final ServicoService servicoService;


    public AgendamentoResourceService(
            TecnicoService tecnicoService,
            ElevadorService elevadorService,
            ServicoService servicoService) {

        this.tecnicoService = tecnicoService;
        this.elevadorService = elevadorService;
        this.servicoService = servicoService;
    }


    public void preencherRecursos(
            Agendamento agendamento,
            AgendamentoRequest request) {

        Tecnico tecnico =
                tecnicoService.buscarPorId(
                        request.getTecnicoId()
                );

        Elevador elevador =
                elevadorService.buscarPorId(
                        request.getElevadorId()
                );


        if (!Boolean.TRUE.equals(tecnico.getAtivo())) {

            throw new RegraNegocioException(
                    "O técnico selecionado está inativo"
            );
        }


        if (!Boolean.TRUE.equals(elevador.getAtivo())) {

            throw new RegraNegocioException(
                    "O elevador selecionado está inativo"
            );
        }


        if (request.getServicosIds() == null
                || request.getServicosIds().isEmpty()) {

            throw new RegraNegocioException(
                    "Selecione pelo menos um serviço"
            );
        }


        agendamento.setTecnico(tecnico);

        agendamento.setElevador(elevador);

        agendamento.setDataHoraInicio(
                request.getDataHoraInicio()
        );


        /*
         * IMPORTANTE PARA EDIÇÃO:
         * remove serviços anteriores.
         */
        agendamento.limparServicos();


        for (Long servicoId :
                request.getServicosIds()
                        .stream()
                        .distinct()
                        .toList()) {

            Servico servico =
                    servicoService.buscarPorId(
                            servicoId
                    );

            if (!Boolean.TRUE.equals(servico.getAtivo())) {

                throw new RegraNegocioException(
                        "O serviço "
                                + servico.getNome()
                                + " está inativo"
                );
            }

            agendamento.adicionarServico(
                    servico
            );
        }
    }
}