package com.oficina.agenda.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "servicos")
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do serviço é obrigatório")
    @Size(max = 100, message = "Nome do serviço deve ter no máximo 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotNull(message = "Duração do serviço é obrigatória")
    @Positive(message = "Duração do serviço deve ser maior que zero")
    @Column(nullable = false)
    private Integer duracaoMinutos;

    @Column(nullable = false)
    private Boolean ativo = true;

    @NotNull(message = "Código é obrigatório")
    @Column(unique = true, nullable = false)
    private Integer codigo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getDuracaoMinutos() {
        return duracaoMinutos;
    }

    public void setDuracaoMinutos(Integer duracaoMinutos) {
        this.duracaoMinutos = duracaoMinutos;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public void desativar() {
        this.ativo = false;
    }

    public void atualizarDados(
            Integer codigo,
            String nome,
            Integer duracaoMinutos,
            Boolean ativo) {

        this.codigo = codigo;
        this.nome = nome;
        this.duracaoMinutos = duracaoMinutos;
        this.ativo = ativo;
    }
}
