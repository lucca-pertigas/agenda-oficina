package com.oficina.agenda.service;

import com.oficina.agenda.model.Tecnico;
import com.oficina.agenda.repository.TecnicoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TecnicoService {

    private final TecnicoRepository tecnicoRepository;

    public TecnicoService(TecnicoRepository tecnicoRepository) {
        this.tecnicoRepository = tecnicoRepository;
    }

    public List<Tecnico> listarTodos() {
        return tecnicoRepository.findAll();
    }

    public Tecnico salvar(Tecnico tecnico) {
        return tecnicoRepository.save(tecnico);
    }

    public Tecnico buscarPorId(Long id) {
        return tecnicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Técnico não encontrado"));
    }

    public Tecnico atualizar(Long id, Tecnico tecnicoAtualizado) {
        Tecnico tecnico = buscarPorId(id);

        tecnico.setNome(tecnicoAtualizado.getNome());
        tecnico.setAtivo(tecnicoAtualizado.getAtivo());

        return tecnicoRepository.save(tecnico);
    }

    public Tecnico desativar (Long id) {
        Tecnico tecnico = buscarPorId(id);

        tecnico.setAtivo(false);

        return tecnicoRepository.save(tecnico);
    }

    public List<Tecnico> listarAtivos() {
        return tecnicoRepository.findByAtivoTrue();
    }
}