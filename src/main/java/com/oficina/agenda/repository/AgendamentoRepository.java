package com.oficina.agenda.repository;

import com.oficina.agenda.model.Agendamento;
import com.oficina.agenda.model.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AgendamentoRepository
        extends JpaRepository<Agendamento, Long> {


    // =========================================================
    // LISTAR POR PERÍODO
    // =========================================================

    List<Agendamento>
    findByStatusNotAndDataHoraInicioLessThanAndDataHoraFimGreaterThanOrderByDataHoraInicioAsc(
            StatusAgendamento status,
            LocalDateTime fimPeriodo,
            LocalDateTime inicioPeriodo
    );


    // =========================================================
    // LISTAR POR STATUS
    // =========================================================

    List<Agendamento>
    findByStatusOrderByDataHoraInicioAsc(
            StatusAgendamento status
    );


    // =========================================================
    // LISTAR POR PERÍODO E ELEVADOR
    // =========================================================

    List<Agendamento>
    findByStatusNotAndElevadorIdAndDataHoraInicioLessThanAndDataHoraFimGreaterThanOrderByDataHoraInicioAsc(
            StatusAgendamento status,
            Long elevadorId,
            LocalDateTime fimPeriodo,
            LocalDateTime inicioPeriodo
    );
}