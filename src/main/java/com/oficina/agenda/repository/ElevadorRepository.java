package com.oficina.agenda.repository;

import com.oficina.agenda.model.Elevador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ElevadorRepository extends JpaRepository<Elevador, Long> {

    List<Elevador> findByAtivoTrue();
}
