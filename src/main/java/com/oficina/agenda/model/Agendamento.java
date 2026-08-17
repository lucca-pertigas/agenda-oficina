package com.oficina.agenda.model;

import com.oficina.agenda.exception.RegraNegocioException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "agendamentos")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do cliente é obrigatório")
    @Column(
            name = "nome_cliente",
            nullable = false,
            length = 120
    )
    private String nomeCliente;

    @NotBlank(message = "Placa do veículo é obrigatória")
    @Column(
            name = "placa_veiculo",
            nullable = false,
            length = 10
    )
    private String placaVeiculo;


    // =========================================
    // MODELO DO VEÍCULO
    // =========================================

    @ManyToOne
    @JoinColumn(
            name = "modelo_veiculo_id",
            nullable = false
    )
    @NotNull(message = "Modelo do veículo é obrigatório")
    private ModeloVeiculo modeloVeiculo;


    // =========================================
    // TÉCNICO
    // =========================================

    @ManyToOne
    @JoinColumn(
            name = "tecnico_id",
            nullable = false
    )
    @NotNull(message = "Técnico é obrigatório")
    private Tecnico tecnico;


    // =========================================
    // ELEVADOR
    // =========================================

    @ManyToOne
    @JoinColumn(
            name = "elevador_id",
            nullable = false
    )
    @NotNull(message = "Elevador é obrigatório")
    private Elevador elevador;


    // =========================================
    // SERVIÇOS
    // =========================================

    @OneToMany(
            mappedBy = "agendamento",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<AgendamentoServico> servicos =
            new ArrayList<>();


    // =========================================
    // HORÁRIOS
    // =========================================

    @NotNull(
            message = "Data e hora de início são obrigatórias"
    )
    private LocalDateTime dataHoraInicio;

    private LocalDateTime dataHoraFim;


    // =========================================
    // STATUS
    // =========================================

    @Enumerated(EnumType.STRING)
    private StatusAgendamento status =
            StatusAgendamento.AGENDADO;


    // =========================================
    // ID
    // =========================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    // =========================================
    // CLIENTE
    // =========================================

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(
            String nomeCliente) {

        this.nomeCliente = nomeCliente;
    }


    // =========================================
    // PLACA
    // =========================================

    public String getPlacaVeiculo() {
        return placaVeiculo;
    }

    public void setPlacaVeiculo(
            String placaVeiculo) {

        this.placaVeiculo = placaVeiculo;
    }


    // =========================================
    // MODELO DO VEÍCULO
    // =========================================

    public ModeloVeiculo getModeloVeiculo() {
        return modeloVeiculo;
    }

    public void setModeloVeiculo(
            ModeloVeiculo modeloVeiculo) {

        this.modeloVeiculo =
                modeloVeiculo;
    }


    // =========================================
    // TÉCNICO
    // =========================================

    public Tecnico getTecnico() {
        return tecnico;
    }

    public void setTecnico(
            Tecnico tecnico) {

        this.tecnico = tecnico;
    }


    // =========================================
    // ELEVADOR
    // =========================================

    public Elevador getElevador() {
        return elevador;
    }

    public void setElevador(
            Elevador elevador) {

        this.elevador = elevador;
    }


    // =========================================
    // SERVIÇOS
    // =========================================

    public List<AgendamentoServico> getServicos() {
        return servicos;
    }

    public void setServicos(
            List<AgendamentoServico> servicos) {

        this.servicos.clear();

        if (servicos == null) {
            return;
        }

        for (AgendamentoServico item : servicos) {

            item.setAgendamento(this);

            this.servicos.add(item);
        }
    }


    public void adicionarServico(
            Servico servico) {

        AgendamentoServico item =
                new AgendamentoServico();

        item.setAgendamento(this);

        item.setServico(servico);

        servicos.add(item);
    }


    public void limparServicos() {

        servicos.clear();
    }


    public int calcularDuracaoTotalMinutos() {

        return servicos
                .stream()
                .map(
                        AgendamentoServico::getServico
                )
                .mapToInt(
                        Servico::getDuracaoMinutos
                )
                .sum();
    }


    // =========================================
    // DATA / HORÁRIO
    // =========================================

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(
            LocalDateTime dataHoraInicio) {

        this.dataHoraInicio =
                dataHoraInicio;
    }


    public LocalDateTime getDataHoraFim() {
        return dataHoraFim;
    }

    public void setDataHoraFim(
            LocalDateTime dataHoraFim) {

        this.dataHoraFim =
                dataHoraFim;
    }


    // =========================================
    // STATUS
    // =========================================

    public StatusAgendamento getStatus() {
        return status;
    }

    public void setStatus(
            StatusAgendamento status) {

        this.status = status;
    }


    // =========================================
    // REGRAS
    // =========================================

    public void validarPodeEditar() {

        if (status == StatusAgendamento.CANCELADO) {

            throw new RegraNegocioException(
                    "Agendamento cancelado não pode ser editado"
            );
        }


        if (status == StatusAgendamento.CONCLUIDO) {

            throw new RegraNegocioException(
                    "Agendamento concluído não pode ser editado"
            );
        }
    }


    public void cancelar() {

        if (status == StatusAgendamento.CANCELADO) {

            throw new RegraNegocioException(
                    "Agendamento já está cancelado"
            );
        }


        status =
                StatusAgendamento.CANCELADO;
    }


    public void concluir() {

        validarPodeEditar();


        status =
                StatusAgendamento.CONCLUIDO;
    }
}