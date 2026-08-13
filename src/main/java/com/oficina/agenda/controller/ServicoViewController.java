package com.oficina.agenda.controller;

import com.oficina.agenda.service.ServicoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ServicoViewController {

    private final ServicoService servicoService;

    public ServicoViewController(ServicoService servicoService) {
        this.servicoService = servicoService;
    }

    @GetMapping("/servicos/tela")
    public String telaServicos(Model model) {

        model.addAttribute(
                "servicos",
                servicoService.listarTodos()
        );

        return "servicos";
    }
}