package com.oficina.agenda.controller;

import com.oficina.agenda.dto.AgendamentoResponse;
import com.oficina.agenda.service.AgendamentoService;
import com.oficina.agenda.dto.AgendamentoRequest;
import jakarta.validation.Valid;
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
    public List<AgendamentoResponse> listarTodos() {
        return agendamentoService.listarTodos();
    }

    @PostMapping
    public AgendamentoResponse cadastrar(
            @Valid @RequestBody AgendamentoRequest request) {

        return agendamentoService.salvar(request);
    }

    @GetMapping("/{id}")
    public AgendamentoResponse buscarPorId(@PathVariable Long id) {
        return agendamentoService.buscarResponsePorId(id);
    }

    @PutMapping("/{id}")
    public AgendamentoResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AgendamentoRequest request) {

        return agendamentoService.atualizar(id, request);
    }

    @PutMapping("/{id}/cancelar")
    public AgendamentoResponse cancelar(@PathVariable Long id) {
        return agendamentoService.cancelar(id);
    }

    @GetMapping("/periodo")
    public List<AgendamentoResponse> listarPorPeriodo(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime inicio,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fim) {

        return agendamentoService.listarPorPeriodo(inicio, fim);
    }

    @GetMapping("/periodo/elevador/{elevadorId}")
    public List<AgendamentoResponse> listarPorPeriodoEElevador(
            @PathVariable Long elevadorId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime inicio,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fim) {

        return agendamentoService.listarPorPeriodoEElevador(
                elevadorId,
                inicio,
                fim
        );
    }

    @GetMapping("/cancelados")
    public List<AgendamentoResponse> listarCancelados() {
        return agendamentoService.listarCancelados();
    }

    @PutMapping("/{id}/concluir")
    public AgendamentoResponse concluir(@PathVariable Long id) {
        return agendamentoService.concluir(id);
    }

    @GetMapping("/agendados")
    public List<AgendamentoResponse> listarAgendados() {
        return agendamentoService.listarAgendados();
    }
}
