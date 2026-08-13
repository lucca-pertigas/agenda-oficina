package com.oficina.agenda.controller;

import com.oficina.agenda.service.ElevadorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ElevadorViewController {

    private final ElevadorService elevadorService;

    public ElevadorViewController(ElevadorService elevadorService) {
        this.elevadorService = elevadorService;
    }

    @GetMapping("/elevadores/tela")
    public String telaElevadores(Model model) {

        model.addAttribute(
                "elevadores",
                elevadorService.listarTodos()
        );

        return "elevadores";
    }
}