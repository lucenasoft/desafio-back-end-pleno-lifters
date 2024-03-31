package com.desafio.urna.Repositories;

import com.desafio.urna.Models.VotoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VotoRepository extends JpaRepository<VotoModel, Long> {
    @Query("SELECT COUNT(v) FROM voto v WHERE v.candidato.id = ?1")
    Long countVotosByCandidatoId(Long candidatoId);
}
