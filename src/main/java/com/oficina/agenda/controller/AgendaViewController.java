package com.oficina.agenda.controller;

import com.oficina.agenda.service.AgendamentoService;
import com.oficina.agenda.service.ElevadorService;
import com.oficina.agenda.service.ServicoService;
import com.oficina.agenda.service.TecnicoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AgendaViewController {

    private final ElevadorService elevadorService;
    private final AgendamentoService agendamentoService;
    private final TecnicoService tecnicoService;
    private final ServicoService servicoService;

    public AgendaViewController(
            ElevadorService elevadorService,
            AgendamentoService agendamentoService,
            TecnicoService tecnicoService,
            ServicoService servicoService) {

        this.elevadorService = elevadorService;
        this.agendamentoService = agendamentoService;
        this.tecnicoService = tecnicoService;
        this.servicoService = servicoService;
    }

    @GetMapping("/agenda")
    public String agenda(
            @RequestParam(required = false) LocalDate data,
            Model model) {

        if (data == null) {
            data = LocalDate.now();
        }

        model.addAttribute(
                "elevadores",
                elevadorService.listarAtivos()
        );

        model.addAttribute(
                "tecnicos",
                tecnicoService.listarAtivos()
        );

        model.addAttribute(
                "servicos",
                servicoService.listarAtivos()
        );

        List<LocalTime> horarios = new ArrayList<>();

        LocalTime horario = LocalTime.of(8, 0);
        LocalTime fechamento = LocalTime.of(17, 0);

        while (horario.isBefore(fechamento)) {

            horarios.add(horario);

            horario = horario.plusMinutes(30);
        }

        model.addAttribute(
                "horarios",
                horarios
        );

        model.addAttribute(
                "data",
                data
        );

        model.addAttribute(
                "dataAnterior",
                data.minusDays(1)
        );

        model.addAttribute(
                "dataProxima",
                data.plusDays(1)
        );

        /*
         * Agora usamos os trechos visuais da agenda.
         *
         * Isso permite:
         *
         * - serviço continuar no dia seguinte;
         * - não contar 12:00 até 13:00;
         * - não contar após 17:00;
         * - dividir o card em manhã e tarde.
         */
        model.addAttribute(
                "agendamentos",
                agendamentoService.listarParaAgenda(data)
        );

        return "agenda";
    }
}