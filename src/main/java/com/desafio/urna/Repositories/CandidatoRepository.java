package com.desafio.urna.Repositories;

import com.desafio.urna.Models.CandidatoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CandidatoRepository extends JpaRepository<CandidatoModel, Integer> {
    @Query("SELECT c FROM candidato c WHERE c.nome = ?1")
    CandidatoModel findByName(String nome);
}
