package com.oficina.agenda.repository;

import com.oficina.agenda.model.Tecnico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TecnicoRepository extends JpaRepository<Tecnico, Long>{

    List<Tecnico> findByAtivoTrue();
}
