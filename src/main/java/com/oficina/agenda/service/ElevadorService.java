package com.oficina.agenda.service;

import com.oficina.agenda.model.Elevador;
import com.oficina.agenda.repository.ElevadorRepository;
import com.oficina.agenda.exception.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElevadorService {

    private final ElevadorRepository elevadorRepository;

    public ElevadorService(ElevadorRepository elevadorRepository) {
        this.elevadorRepository = elevadorRepository;
    }

    public List<Elevador> listarTodos() {
        return elevadorRepository.findAll();
    }

    public List<Elevador> listarAtivos() {
        return elevadorRepository.findByAtivoTrue();
    }

    public Elevador salvar(Elevador elevador) {
        return elevadorRepository.save(elevador);
    }

    public Elevador buscarPorId(Long id) {
        return elevadorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Elevador não encontrado"
                ));
    }

    public Elevador atualizar(Long id, Elevador elevadorAtualizado) {
        Elevador elevador = buscarPorId(id);

        elevador.setNumero(elevadorAtualizado.getNumero());
        elevador.setAtivo(elevadorAtualizado.getAtivo());

        return elevadorRepository.save(elevador);
    }

    public Elevador desativar(Long id) {
        Elevador elevador = buscarPorId(id);

        elevador.setAtivo(false);

        return elevadorRepository.save(elevador);
    }
}
