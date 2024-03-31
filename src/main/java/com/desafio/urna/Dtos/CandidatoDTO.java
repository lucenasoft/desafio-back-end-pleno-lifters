package com.desafio.urna.Dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CandidatoDTO {

    @NotBlank
    @Size(min = 1, max = 100)
    private String nome;

    public CandidatoDTO() {
    }

    public CandidatoDTO(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
