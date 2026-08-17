package com.oficina.agenda.repository;

import com.oficina.agenda.model.ModeloVeiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModeloVeiculoRepository
        extends JpaRepository<ModeloVeiculo, Long> {

    List<ModeloVeiculo>
    findByAtivoTrueOrderByNomeAsc();

    List<ModeloVeiculo>
    findAllByOrderByNomeAsc();

    boolean existsByNomeIgnoreCase(
            String nome
    );
}