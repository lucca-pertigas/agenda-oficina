package com.oficina.agenda.controller;

import com.oficina.agenda.model.Servico;
import com.oficina.agenda.service.ServicoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    private final ServicoService servicoService;

    public ServicoController(ServicoService servicoService) {
        this.servicoService = servicoService;
    }

    @GetMapping
    public List<Servico> listarTodos() {
        return servicoService.listarTodos();
    }

    @PostMapping
    public Servico cadastrar (@Valid @RequestBody Servico servico) {
        return servicoService.salvar(servico);
    }

    @GetMapping("/{id}")
    public Servico buscarPorId(@PathVariable Long id) {
        return servicoService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Servico atualizar (
            @PathVariable Long id,
            @Valid @RequestBody Servico servico
    ) {
        return servicoService.atualizar(id, servico);
    }

    @PutMapping("/{id}/desativar")
    public Servico desativar(@PathVariable Long id) {
        return servicoService.desativar(id);
    }

    @GetMapping("/ativos")
    public List<Servico> listarAtivos() {
        return servicoService.listarAtivos();
    }

}
