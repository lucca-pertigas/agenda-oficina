package com.oficina.agenda.controller;

import com.oficina.agenda.model.IndisponibilidadeTecnico;
import com.oficina.agenda.service.IndisponibilidadeTecnicoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/indisponibilidades-tecnicos")
public class IndisponibilidadeTecnicoController {

    private final IndisponibilidadeTecnicoService indisponibilidadeTecnicoService;

    public IndisponibilidadeTecnicoController(
            IndisponibilidadeTecnicoService indisponibilidadeTecnicoService) {

        this.indisponibilidadeTecnicoService =
                indisponibilidadeTecnicoService;
    }


    @GetMapping
    public List<IndisponibilidadeTecnico> listarTodos() {

        return indisponibilidadeTecnicoService
                .listarTodos();
    }


    @GetMapping("/tecnico/{tecnicoId}")
    public List<IndisponibilidadeTecnico> listarPorTecnico(
            @PathVariable Long tecnicoId) {

        return indisponibilidadeTecnicoService
                .listarPorTecnico(tecnicoId);
    }


    @PostMapping
    public IndisponibilidadeTecnico cadastrar(
            @Valid
            @RequestBody IndisponibilidadeTecnico indisponibilidade) {

        return indisponibilidadeTecnicoService
                .salvar(indisponibilidade);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long id) {

        indisponibilidadeTecnicoService
                .excluir(id);

        return ResponseEntity.noContent().build();
    }
}