package com.oficina.agenda.controller;

import com.oficina.agenda.service.ElevadorService;
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

    public AgendaViewController(ElevadorService elevadorService) {
        this.elevadorService = elevadorService;
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

        List<LocalTime> horarios = new ArrayList<>();

        LocalTime inicio = LocalTime.of(8, 0);
        LocalTime fim = LocalTime.of(18, 0);

        while (!inicio.isAfter(fim)) {
            horarios.add(inicio);
            inicio = inicio.plusHours(1);
        }

        model.addAttribute("horarios", horarios);

        model.addAttribute("data", data);
        model.addAttribute("dataAnterior", data.minusDays(1));
        model.addAttribute("dataProxima", data.plusDays(1));

        return "agenda";
    }
}