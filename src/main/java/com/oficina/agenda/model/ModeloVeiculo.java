package com.oficina.agenda.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "modelos_veiculo")
public class ModeloVeiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do modelo é obrigatório")
    @Column(
            nullable = false,
            unique = true,
            length = 100
    )
    private String nome;

    @Column(nullable = false)
    private Boolean ativo = true;


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


    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {

        this.ativo = ativo;
    }


    public void desativar() {

        this.ativo = false;
    }
}