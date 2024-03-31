package com.desafio.urna.Repositories;

import com.desafio.urna.Models.CandidatoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CandidatoRepository extends JpaRepository<CandidatoModel, Long> {
    @Query("SELECT c FROM candidato c WHERE c.nome = ?1")
    CandidatoModel findByName(String nome);

    @Query("SELECT c FROM candidato c WHERE c.cargo.id = ?1")
    List<CandidatoModel> findByCargoId(Long cargoId);
}
