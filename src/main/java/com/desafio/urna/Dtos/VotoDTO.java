package com.desafio.urna.Dtos;

import com.desafio.urna.Models.CandidatoModel;
import com.desafio.urna.Models.EleitorModel;
import jakarta.validation.constraints.NotBlank;

public class VotoDTO {
    @NotBlank
    private Long id;

    @NotBlank
    private EleitorModel eleitor;

    @NotBlank
    private CandidatoModel candidato;

    public VotoDTO() {
    }

    public VotoDTO(Long id, EleitorModel eleitor, CandidatoModel candidato) {
        this.id = id;
        this.eleitor = eleitor;
        this.candidato = candidato;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EleitorModel getEleitor() {
        return eleitor;
    }

    public void setEleitor(EleitorModel eleitor) {
        this.eleitor = eleitor;
    }

    public CandidatoModel getCandidato() {
        return candidato;
    }

    public void setCandidato(CandidatoModel candidato) {
        this.candidato = candidato;
    }
}
