package com.desafio.urna.Dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CandidatoDTO {

    @NotBlank
    @Size(min = 1, max = 100)
    private String nome;

    @NotBlank
    @Size(min = 1, max = 100)
    private String partido;

    @NotNull
    @Min(0)
    private int numero;

    @Min(0)
    private int votos;

    public CandidatoDTO() {
    }

    public CandidatoDTO(String nome, String partido, int numero, int votos) {
        this.nome = nome;
        this.partido = partido;
        this.numero = numero;
        this.votos = votos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPartido() {
        return partido;
    }

    public void setPartido(String partido) {
        this.partido = partido;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getVotos() {
        return votos;
    }

    public void setVotos(int votos) {
        this.votos = votos;
    }
}
