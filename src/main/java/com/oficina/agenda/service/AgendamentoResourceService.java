package com.oficina.agenda.service;

import com.oficina.agenda.dto.AgendamentoRequest;
import com.oficina.agenda.exception.RegraNegocioException;
import com.oficina.agenda.model.Agendamento;
import com.oficina.agenda.model.Elevador;
import com.oficina.agenda.model.ModeloVeiculo;
import com.oficina.agenda.model.Servico;
import com.oficina.agenda.model.Tecnico;
import org.springframework.stereotype.Service;

@Service
public class AgendamentoResourceService {

    private final TecnicoService tecnicoService;
    private final ElevadorService elevadorService;
    private final ServicoService servicoService;
    private final ModeloVeiculoService modeloVeiculoService;


    public AgendamentoResourceService(
            TecnicoService tecnicoService,
            ElevadorService elevadorService,
            ServicoService servicoService,
            ModeloVeiculoService modeloVeiculoService) {

        this.tecnicoService = tecnicoService;
        this.elevadorService = elevadorService;
        this.servicoService = servicoService;
        this.modeloVeiculoService = modeloVeiculoService;
    }


    public void preencherRecursos(
            Agendamento agendamento,
            AgendamentoRequest request) {

        // =========================================
        // VALIDAÇÕES BÁSICAS
        // =========================================

        if (request.getNomeCliente() == null
                || request.getNomeCliente().trim().isEmpty()) {

            throw new RegraNegocioException(
                    "Nome do cliente é obrigatório"
            );
        }


        if (request.getPlacaVeiculo() == null
                || request.getPlacaVeiculo().trim().isEmpty()) {

            throw new RegraNegocioException(
                    "Placa do veículo é obrigatória"
            );
        }


        if (request.getModeloVeiculoId() == null) {

            throw new RegraNegocioException(
                    "Modelo do veículo é obrigatório"
            );
        }


        if (request.getServicosIds() == null
                || request.getServicosIds().isEmpty()) {

            throw new RegraNegocioException(
                    "Selecione pelo menos um serviço"
            );
        }


        // =========================================
        // BUSCAR MODELO DO VEÍCULO
        // =========================================

        ModeloVeiculo modeloVeiculo =
                modeloVeiculoService.buscarPorId(
                        request.getModeloVeiculoId()
                );


        if (!Boolean.TRUE.equals(modeloVeiculo.getAtivo())) {

            throw new RegraNegocioException(
                    "O modelo de veículo selecionado está inativo"
            );
        }


        // =========================================
        // BUSCAR TÉCNICO
        // =========================================

        Tecnico tecnico =
                tecnicoService.buscarPorId(
                        request.getTecnicoId()
                );


        if (!Boolean.TRUE.equals(tecnico.getAtivo())) {

            throw new RegraNegocioException(
                    "O técnico selecionado está inativo"
            );
        }


        // =========================================
        // BUSCAR ELEVADOR
        // =========================================

        Elevador elevador =
                elevadorService.buscarPorId(
                        request.getElevadorId()
                );


        if (!Boolean.TRUE.equals(elevador.getAtivo())) {

            throw new RegraNegocioException(
                    "O elevador selecionado está inativo"
            );
        }


        // =========================================
        // DADOS DO CLIENTE / VEÍCULO
        // =========================================

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


        // =========================================
        // RECURSOS DO AGENDAMENTO
        // =========================================

        agendamento.setTecnico(
                tecnico
        );


        agendamento.setElevador(
                elevador
        );


        agendamento.setDataHoraInicio(
                request.getDataHoraInicio()
        );


        // =========================================
        // SERVIÇOS
        // =========================================

        /*
         * Importante para edição:
         * remove os serviços antigos antes
         * de adicionar os novos.
         */
        agendamento.limparServicos();


        /*
         * distinct() evita o mesmo serviço
         * aparecer duas vezes no agendamento.
         */
        for (Long servicoId :
                request
                        .getServicosIds()
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