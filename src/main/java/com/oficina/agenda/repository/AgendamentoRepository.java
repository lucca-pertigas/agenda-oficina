package com.oficina.agenda.repository;

import com.oficina.agenda.model.Agendamento;
import com.oficina.agenda.model.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    boolean existsByElevadorIdAndStatusNotAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(
            Long elevadorId,
            StatusAgendamento status,
            LocalDateTime dataHoraFim,
            LocalDateTime dataHoraInicio
    );

    boolean existsByTecnicoIdAndStatusNotAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(
            Long tecnicoId,
            StatusAgendamento status,
            LocalDateTime dataHoraFim,
            LocalDateTime dataHoraInicio
    );

    boolean existsByElevadorIdAndIdNotAndStatusNotAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(
            Long elevadorId,
            Long id,
            StatusAgendamento status,
            LocalDateTime dataHoraFim,
            LocalDateTime dataHoraInicio
    );

    boolean existsByTecnicoIdAndIdNotAndStatusNotAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(
            Long tecnicoId,
            Long id,
            StatusAgendamento status,
            LocalDateTime dataHoraFim,
            LocalDateTime dataHoraInicio
    );
}