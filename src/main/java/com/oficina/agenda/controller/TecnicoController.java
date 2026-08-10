package com.oficina.agenda.controller;

import com.oficina.agenda.model.Tecnico;
import com.oficina.agenda.service.TecnicoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tecnicos")
public class TecnicoController {

    private final TecnicoService tecnicoService;

    public TecnicoController(TecnicoService tecnicoService) {
        this.tecnicoService = tecnicoService;
    }

    @GetMapping
    public List<Tecnico> listarTodos() {
        return tecnicoService.listarTodos();
    }

    @PostMapping
    public Tecnico cadastrar(@Valid @RequestBody Tecnico tecnico) {

        return tecnicoService.salvar(tecnico);
    }

    @GetMapping("/{id}")
    public Tecnico buscarPorId(@PathVariable Long id) {
        return tecnicoService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Tecnico atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Tecnico tecnico) {

        return tecnicoService.atualizar(id, tecnico);
    }

    @PutMapping("/{id}/desativar")
    public Tecnico desativar(@PathVariable Long id) {

        return tecnicoService.desativar(id);
    }

    @GetMapping("/ativos")
    public List<Tecnico> listarAtivos() {

        return tecnicoService.listarAtivos();
    }
}