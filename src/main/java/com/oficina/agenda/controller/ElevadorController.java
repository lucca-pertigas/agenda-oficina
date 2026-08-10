package com.oficina.agenda.controller;

import com.oficina.agenda.model.Elevador;
import com.oficina.agenda.service.ElevadorService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/elevadores")
public class ElevadorController {

    private final ElevadorService elevadorService;

    public ElevadorController(ElevadorService elevadorService) {
        this.elevadorService = elevadorService;
    }

    @GetMapping
    public List<Elevador> listarTodos() {
        return elevadorService.listarTodos();
    }

    @PostMapping
    public Elevador cadastrar(@Valid @RequestBody Elevador elevador) {
        return elevadorService.salvar(elevador);
    }

    @GetMapping("/{id}")
    public Elevador buscarPorId(@PathVariable Long id) {
        return elevadorService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Elevador atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Elevador elevador) {

        return elevadorService.atualizar(id, elevador);
    }

    @PutMapping("/{id}/desativar")
    public Elevador desativar(@PathVariable Long id) {
        return elevadorService.desativar(id);
    }

    @GetMapping("/ativos")
    public List<Elevador> listarAtivos() {
        return elevadorService.listarAtivos();
    }
}
