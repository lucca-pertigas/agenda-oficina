package com.oficina.agenda.service;

import com.oficina.agenda.exception.RecursoNaoEncontradoException;
import com.oficina.agenda.exception.RegraNegocioException;
import com.oficina.agenda.model.ModeloVeiculo;
import com.oficina.agenda.repository.ModeloVeiculoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModeloVeiculoService {

    private final ModeloVeiculoRepository
            modeloVeiculoRepository;


    public ModeloVeiculoService(
            ModeloVeiculoRepository modeloVeiculoRepository) {

        this.modeloVeiculoRepository =
                modeloVeiculoRepository;
    }


    public List<ModeloVeiculo> listarTodos() {

        return modeloVeiculoRepository
                .findAllByOrderByNomeAsc();
    }


    public List<ModeloVeiculo> listarAtivos() {

        return modeloVeiculoRepository
                .findByAtivoTrueOrderByNomeAsc();
    }


    public ModeloVeiculo buscarPorId(
            Long id) {

        return modeloVeiculoRepository
                .findById(id)
                .orElseThrow(
                        () ->
                                new RecursoNaoEncontradoException(
                                        "Modelo de veículo não encontrado"
                                )
                );
    }


    public ModeloVeiculo salvar(
            ModeloVeiculo modelo) {

        if (modelo.getNome() == null
                || modelo.getNome().trim().isEmpty()) {

            throw new RegraNegocioException(
                    "Nome do modelo é obrigatório"
            );
        }


        String nome =
                modelo
                        .getNome()
                        .trim();


        if (
                modeloVeiculoRepository
                        .existsByNomeIgnoreCase(
                                nome
                        )
        ) {

            throw new RegraNegocioException(
                    "Já existe um modelo cadastrado com este nome"
            );
        }


        modelo.setNome(
                nome
        );

        modelo.setAtivo(
                true
        );


        return modeloVeiculoRepository
                .save(modelo);
    }


    public ModeloVeiculo atualizar(
            Long id,
            ModeloVeiculo atualizado) {

        ModeloVeiculo modelo =
                buscarPorId(id);


        if (atualizado.getNome() == null
                || atualizado
                .getNome()
                .trim()
                .isEmpty()) {

            throw new RegraNegocioException(
                    "Nome do modelo é obrigatório"
            );
        }


        modelo.setNome(
                atualizado
                        .getNome()
                        .trim()
        );


        modelo.setAtivo(
                atualizado.getAtivo()
        );


        return modeloVeiculoRepository
                .save(modelo);
    }


    public ModeloVeiculo desativar(
            Long id) {

        ModeloVeiculo modelo =
                buscarPorId(id);


        modelo.desativar();


        return modeloVeiculoRepository
                .save(modelo);
    }
}