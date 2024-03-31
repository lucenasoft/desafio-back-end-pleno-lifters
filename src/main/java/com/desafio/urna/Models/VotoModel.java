package com.desafio.urna.Models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity(name = "voto")
public class VotoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, hidden = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidato_id")
    private EleitorModel eleitor;

    @ManyToOne
    @JoinColumn(name = "eleitor_id")
    private CandidatoModel candidato;

    public VotoModel() {
        }

    public VotoModel(Long id, EleitorModel eleitor, CandidatoModel candidato) {
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
