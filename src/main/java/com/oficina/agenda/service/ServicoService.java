package com.oficina.agenda.service;

import com.oficina.agenda.model.Servico;
import com.oficina.agenda.repository.ServicoRepository;
import com.oficina.agenda.exception.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicoService {

    private final ServicoRepository servicoRepository;

    public ServicoService(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    public List<Servico> listarTodos() {
        return servicoRepository.findAll();
    }

    public List<Servico> listarAtivos() {
        return servicoRepository.findByAtivoTrue();
    }

    public Servico salvar(Servico servico) {
        return servicoRepository.save(servico);
    }

    public Servico buscarPorId(Long id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Serviço não encontrado"
                ));
    }

    public Servico atualizar(Long id, Servico servicoAtualizado) {
        Servico servico = buscarPorId(id);

        servico.setNome(servicoAtualizado.getNome());
        servico.setDuracaoMinutos(servicoAtualizado.getDuracaoMinutos());
        servico.setAtivo(servicoAtualizado.getAtivo());

        return servicoRepository.save(servico);
    }

    public Servico desativar(Long id) {
        Servico servico = buscarPorId(id);

        servico.setAtivo(false);

        return servicoRepository.save(servico);
    }
}
