package com.oficina.agenda.service.resource;

import com.oficina.agenda.exception.RegraNegocioException;
import com.oficina.agenda.model.Elevador;
import com.oficina.agenda.model.ModeloVeiculo;
import com.oficina.agenda.model.Servico;
import com.oficina.agenda.model.Tecnico;
import com.oficina.agenda.service.ElevadorService;
import com.oficina.agenda.service.ModeloVeiculoService;
import com.oficina.agenda.service.ServicoService;
import com.oficina.agenda.service.TecnicoService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AgendamentoResourceResolver {

    private final TecnicoService tecnicoService;
    private final ElevadorService elevadorService;
    private final ModeloVeiculoService modeloVeiculoService;
    private final ServicoService servicoService;


    public AgendamentoResourceResolver(
            TecnicoService tecnicoService,
            ElevadorService elevadorService,
            ModeloVeiculoService modeloVeiculoService,
            ServicoService servicoService) {

        this.tecnicoService = tecnicoService;
        this.elevadorService = elevadorService;
        this.modeloVeiculoService = modeloVeiculoService;
        this.servicoService = servicoService;
    }


    public Tecnico resolverTecnico(Long id) {

        if (id == null) {
            return null;
        }

        Tecnico tecnico =
                tecnicoService.buscarPorId(id);

        if (!Boolean.TRUE.equals(tecnico.getAtivo())) {

            throw new RegraNegocioException(
                    "O técnico selecionado está inativo"
            );
        }

        return tecnico;
    }


    public Elevador resolverElevador(Long id) {

        Elevador elevador =
                elevadorService.buscarPorId(id);

        if (!Boolean.TRUE.equals(elevador.getAtivo())) {

            throw new RegraNegocioException(
                    "O elevador selecionado está inativo"
            );
        }

        return elevador;
    }


    public ModeloVeiculo resolverModeloVeiculo(Long id) {

        ModeloVeiculo modelo =
                modeloVeiculoService.buscarPorId(id);

        if (!Boolean.TRUE.equals(modelo.getAtivo())) {

            throw new RegraNegocioException(
                    "O modelo de veículo selecionado está inativo"
            );
        }

        return modelo;
    }


    public List<Servico> resolverServicos(
            List<Long> servicosIds) {

        if (servicosIds == null
                || servicosIds.isEmpty()) {

            throw new RegraNegocioException(
                    "Selecione pelo menos um serviço"
            );
        }


        return servicosIds
                .stream()
                .distinct()
                .map(servicoService::buscarPorId)
                .peek(
                        servico -> {

                            if (!Boolean.TRUE.equals(
                                    servico.getAtivo()
                            )) {

                                throw new RegraNegocioException(
                                        "O serviço "
                                                + servico.getNome()
                                                + " está inativo"
                                );
                            }
                        }
                )
                .toList();
    }
}