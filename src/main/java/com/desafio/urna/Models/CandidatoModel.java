package com.desafio.urna.Models;

import jakarta.persistence.*;

@Entity(name = "candidato")
public class CandidatoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @ManyToOne
    @JoinColumn(name = "cargo_id")
    private CargoModel cargo;

    public CandidatoModel() {
    }

    public CandidatoModel(String nome) {
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
