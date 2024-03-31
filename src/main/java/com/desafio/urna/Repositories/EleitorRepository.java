package com.desafio.urna.Repositories;

import com.desafio.urna.Models.EleitorModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EleitorRepository extends JpaRepository<EleitorModel, Long> {
    @Query("SELECT e FROM eleitor e WHERE e.nome = ?1")
    EleitorModel findByName(String nome);
}
