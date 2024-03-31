package com.desafio.urna.Dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EleitorDTO {
    @NotBlank
    @Size(min = 1, max = 100)
    private String nome;

    public EleitorDTO() {
    }

    public EleitorDTO(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}