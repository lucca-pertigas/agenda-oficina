package com.oficina.agenda.repository;

import com.oficina.agenda.model.Agendamento;
import com.oficina.agenda.model.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AgendamentoRepository
        extends JpaRepository<Agendamento, Long> {

    List<Agendamento>
    findByStatusNotAndDataHoraInicioLessThanAndDataHoraFimGreaterThanOrderByDataHoraInicioAsc(
            StatusAgendamento status,
            LocalDateTime fimPeriodo,
            LocalDateTime inicioPeriodo
    );
}