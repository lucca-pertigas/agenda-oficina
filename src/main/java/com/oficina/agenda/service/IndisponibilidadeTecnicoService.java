package com.oficina.agenda.service;

import com.oficina.agenda.exception.RecursoNaoEncontradoException;
import com.oficina.agenda.exception.RegraNegocioException;
import com.oficina.agenda.model.IndisponibilidadeTecnico;
import com.oficina.agenda.repository.IndisponibilidadeTecnicoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class IndisponibilidadeTecnicoService {

    private final IndisponibilidadeTecnicoRepository repository;

    public IndisponibilidadeTecnicoService(
            IndisponibilidadeTecnicoRepository repository) {

        this.repository = repository;
    }


    public IndisponibilidadeTecnico salvar(
            IndisponibilidadeTecnico indisponibilidade) {

        if (indisponibilidade.getDataInicio() == null) {
            throw new RegraNegocioException(
                    "Data inicial é obrigatória"
            );
        }

        if (indisponibilidade.getDataFim() == null) {
            throw new RegraNegocioException(
                    "Data final é obrigatória"
            );
        }

        if (indisponibilidade.getDataFim()
                .isBefore(indisponibilidade.getDataInicio())) {

            throw new RegraNegocioException(
                    "A data final não pode ser anterior à data inicial"
            );
        }

        if (indisponibilidade.getTecnico() == null
                || indisponibilidade.getTecnico().getId() == null) {

            throw new RegraNegocioException(
                    "Técnico é obrigatório"
            );
        }

        return repository.save(indisponibilidade);
    }


    public List<IndisponibilidadeTecnico> listarTodos() {

        return repository.findAll();
    }


    public List<IndisponibilidadeTecnico> listarPorTecnico(
            Long tecnicoId) {

        return repository
                .findByTecnicoIdOrderByDataInicioAsc(
                        tecnicoId
                );
    }


    public boolean tecnicoIndisponivel(
            Long tecnicoId,
            LocalDate data) {

        return repository
                .existsByTecnicoIdAndDataInicioLessThanEqualAndDataFimGreaterThanEqual(
                        tecnicoId,
                        data,
                        data
                );
    }


    public void excluir(Long id) {

        if (!repository.existsById(id)) {

            throw new RecursoNaoEncontradoException(
                    "Indisponibilidade não encontrada"
            );
        }

        repository.deleteById(id);
    }
}