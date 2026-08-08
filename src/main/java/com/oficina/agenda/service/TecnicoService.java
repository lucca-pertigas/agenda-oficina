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
}
