package com.desafio.urna.Models;

import jakarta.persistence.*;

@Entity(name = "candidato")
public class CandidatoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "partido")
    private String partido;

    @Column(name = "numero")
    private int numero;

    @Column(name = "votos")
    private int votos;

    public CandidatoModel() {
    }

    public CandidatoModel(String nome, String partido, int numero, int votos) {
        this.nome = nome;
        this.partido = partido;
        this.numero = numero;
        this.votos = votos;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getPartido() {
        return partido;
    }

    public int getNumero() {
        return numero;
    }

    public int getVotos() {
        return votos;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPartido(String partido) {
        this.partido = partido;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setVotos(int votos) {
        this.votos = votos;
    }
}
