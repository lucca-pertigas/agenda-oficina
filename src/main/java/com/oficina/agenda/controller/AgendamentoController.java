package com.oficina.agenda.controller;

import com.oficina.agenda.model.Agendamento;
import com.oficina.agenda.service.AgendamentoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @GetMapping
    public List<Agendamento> listarTodos() {
        return agendamentoService.listarTodos();
    }

    @PostMapping
    public Agendamento cadastrar(@RequestBody Agendamento agendamento) {
        return agendamentoService.salvar(agendamento);
    }

    @GetMapping("/{id}")
    public Agendamento buscarPorId(@PathVariable Long id) {
        return agendamentoService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Agendamento atualizar (
            @PathVariable Long id,
            @RequestBody Agendamento agendamento
    ) {

        return agendamentoService.atualizar(id, agendamento);
    }

    @PutMapping("/{id}/cancelar")
    public Agendamento cancelar(@PathVariable Long id) {
        return agendamentoService.cancelar(id);
    }
}
