package com.oficina.agenda.service;

import com.oficina.agenda.exception.RecursoNaoEncontradoException;
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

    public List<Tecnico> listarAtivos() {
        return tecnicoRepository.findByAtivoTrue();
    }

    public Tecnico buscarPorId(Long id) {
        return tecnicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Técnico não encontrado"
                ));
    }

    public Tecnico salvar(Tecnico tecnico) {
        return tecnicoRepository.save(tecnico);
    }

    public Tecnico atualizar(
            Long id,
            Tecnico tecnicoAtualizado) {

        Tecnico tecnico = buscarPorId(id);

        tecnico.atualizarDados(
                tecnicoAtualizado.getNome(),
                tecnicoAtualizado.getAtivo()
        );

        return tecnicoRepository.save(tecnico);
    }

    public Tecnico desativar(Long id) {

        Tecnico tecnico = buscarPorId(id);

        tecnico.desativar();

        return tecnicoRepository.save(tecnico);
    }
}