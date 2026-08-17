package com.oficina.agenda.controller;

import com.oficina.agenda.model.ModeloVeiculo;
import com.oficina.agenda.service.ModeloVeiculoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/modelos-veiculo")
public class ModeloVeiculoController {

    private final ModeloVeiculoService modeloVeiculoService;


    public ModeloVeiculoController(
            ModeloVeiculoService modeloVeiculoService) {

        this.modeloVeiculoService =
                modeloVeiculoService;
    }


    // =========================================
    // TELA
    // =========================================

    @GetMapping("/tela")
    public String tela(Model model) {

        model.addAttribute(
                "modelos",
                modeloVeiculoService.listarTodos()
        );

        return "modelos-veiculo";
    }


    // =========================================
    // LISTAR TODOS
    // =========================================

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<ModeloVeiculo>> listarTodos() {

        return ResponseEntity.ok(
                modeloVeiculoService.listarTodos()
        );
    }


    // =========================================
    // LISTAR ATIVOS
    // =========================================

    @GetMapping("/ativos")
    @ResponseBody
    public ResponseEntity<List<ModeloVeiculo>> listarAtivos() {

        return ResponseEntity.ok(
                modeloVeiculoService.listarAtivos()
        );
    }


    // =========================================
    // BUSCAR POR ID
    // =========================================

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ModeloVeiculo> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                modeloVeiculoService.buscarPorId(id)
        );
    }


    // =========================================
    // CADASTRAR
    // =========================================

    @PostMapping
    @ResponseBody
    public ResponseEntity<ModeloVeiculo> cadastrar(
            @Valid
            @RequestBody ModeloVeiculo modelo) {

        return ResponseEntity.ok(
                modeloVeiculoService.salvar(modelo)
        );
    }


    // =========================================
    // ATUALIZAR
    // =========================================

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ModeloVeiculo> atualizar(
            @PathVariable Long id,
            @Valid
            @RequestBody ModeloVeiculo modelo) {

        return ResponseEntity.ok(
                modeloVeiculoService.atualizar(
                        id,
                        modelo
                )
        );
    }


    // =========================================
    // DESATIVAR
    // =========================================

    @PutMapping("/{id}/desativar")
    @ResponseBody
    public ResponseEntity<ModeloVeiculo> desativar(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                modeloVeiculoService.desativar(id)
        );
    }
}