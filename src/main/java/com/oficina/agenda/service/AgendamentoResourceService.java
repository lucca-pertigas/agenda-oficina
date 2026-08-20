package com.oficina.agenda.service;

import com.oficina.agenda.dto.AgendamentoRequest;
import com.oficina.agenda.model.Agendamento;
import com.oficina.agenda.model.Elevador;
import com.oficina.agenda.model.ModeloVeiculo;
import com.oficina.agenda.model.Servico;
import com.oficina.agenda.model.Tecnico;
import com.oficina.agenda.service.resource.AgendamentoResourceResolver;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendamentoResourceService {

    private final AgendamentoResourceResolver resourceResolver;


    public AgendamentoResourceService(
            AgendamentoResourceResolver resourceResolver) {

        this.resourceResolver =
                resourceResolver;
    }


    public void preencherRecursos(
            Agendamento agendamento,
            AgendamentoRequest request) {

        ModeloVeiculo modeloVeiculo =
                resourceResolver
                        .resolverModeloVeiculo(
                                request.getModeloVeiculoId()
                        );


        Tecnico tecnico =
                resourceResolver
                        .resolverTecnico(
                                request.getTecnicoId()
                        );


        Elevador elevador =
                resourceResolver
                        .resolverElevador(
                                request.getElevadorId()
                        );


        List<Servico> servicos =
                resourceResolver
                        .resolverServicos(
                                request.getServicosIds()
                        );


        agendamento.setNomeCliente(
                request
                        .getNomeCliente()
                        .trim()
        );


        agendamento.setPlacaVeiculo(
                request
                        .getPlacaVeiculo()
                        .trim()
                        .toUpperCase()
        );


        agendamento.setModeloVeiculo(
                modeloVeiculo
        );


        agendamento.setTecnico(
                tecnico
        );


        agendamento.setElevador(
                elevador
        );


        agendamento.setDataHoraInicio(
                request.getDataHoraInicio()
        );


        agendamento
                .limparServicos();


        for (Servico servico : servicos) {

            agendamento
                    .adicionarServico(
                            servico
                    );
        }
    }
}