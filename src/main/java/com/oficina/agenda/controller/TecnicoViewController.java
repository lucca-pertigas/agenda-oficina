package com.oficina.agenda.controller;

import com.oficina.agenda.service.TecnicoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TecnicoViewController {

    private final TecnicoService tecnicoService;

    public TecnicoViewController(TecnicoService tecnicoService) {
        this.tecnicoService = tecnicoService;
    }

    @GetMapping("/tecnicos/tela")
    public String telaTecnicos(Model model) {

        model.addAttribute(
                "tecnicos",
                tecnicoService.listarTodos()
        );

        return "tecnicos";
    }
}