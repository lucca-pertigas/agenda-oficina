package com.oficina.agenda.repository;

import com.oficina.agenda.model.IndisponibilidadeTecnico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface IndisponibilidadeTecnicoRepository
        extends JpaRepository<IndisponibilidadeTecnico, Long> {

    boolean existsByTecnicoIdAndDataInicioLessThanEqualAndDataFimGreaterThanEqual(
            Long tecnicoId,
            LocalDate dataInicio,
            LocalDate dataFim
    );

    List<IndisponibilidadeTecnico>
    findByTecnicoIdOrderByDataInicioAsc(Long tecnicoId);
}