package com.oficina.agenda.repository;

import com.oficina.agenda.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicoRepository extends JpaRepository<Servico, Long> {

    List<Servico> findByAtivoTrue();

    boolean existsByCodigo(Integer codigo);

    boolean existsByCodigoAndIdNot(
            Integer codigo,
            Long id
    );
}