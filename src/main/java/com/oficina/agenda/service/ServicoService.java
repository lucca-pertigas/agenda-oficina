package com.oficina.agenda.service;

import com.oficina.agenda.exception.RecursoNaoEncontradoException;
import com.oficina.agenda.exception.RegraNegocioException;
import com.oficina.agenda.model.Servico;
import com.oficina.agenda.repository.ServicoRepository;
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

        if (servicoRepository.existsByCodigo(servico.getCodigo())) {
            throw new RegraNegocioException(
                    "Já existe um serviço com esse código"
            );
        }

        return servicoRepository.save(servico);
    }

    public Servico buscarPorId(Long id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Serviço não encontrado"
                ));
    }

    public Servico atualizar(
            Long id,
            Servico servicoAtualizado) {

        Servico servico = buscarPorId(id);

        if (servicoRepository.existsByCodigoAndIdNot(
                servicoAtualizado.getCodigo(),
                id
        )) {
            throw new RegraNegocioException(
                    "Já existe outro serviço com esse código"
            );
        }

        servico.atualizarDados(
                servicoAtualizado.getCodigo(),
                servicoAtualizado.getNome(),
                servicoAtualizado.getDuracaoMinutos(),
                servicoAtualizado.getAtivo()
        );

        return servicoRepository.save(servico);
    }

    public Servico desativar(Long id) {

        Servico servico = buscarPorId(id);

        servico.desativar();

        return servicoRepository.save(servico);
    }
}