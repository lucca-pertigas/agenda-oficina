package com.oficina.agenda.controller;

import com.oficina.agenda.model.Agendamento;
import com.oficina.agenda.service.AgendamentoService;
import jdk.jfr.DataAmount;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @GetMapping("/periodo")
    public List<Agendamento> listarPorPeriodo(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime inicio,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fim) {

        return agendamentoService.listarPorPeriodo(inicio, fim);
    }

    @GetMapping("/periodo/elevador/{elevadorID}")
    public List<Agendamento> listarPorPeriodoEElevador (
            @PathVariable Long elevadorID,
            @PathVariable LocalDateTime inicio,
            @PathVariable LocalDateTime fim) {

        return agendamentoService.listarPorPeriodoEElevador(
                elevadorID,
                inicio,
                fim
        );
    }

    @GetMapping("/cancelados")
    public List<Agendamento> listarCancelados() {
        return agendamentoService.listarCancelados();
    }

    @PutMapping("/{id}/concluir")
    public Agendamento concluir(@PathVariable Long id) {
        return agendamentoService.concluir(id);
    }
}
