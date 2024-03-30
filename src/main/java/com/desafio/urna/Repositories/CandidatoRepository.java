package com.desafio.urna.Repositories;

import com.desafio.urna.Models.CandidatoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidatoRepository extends JpaRepository<CandidatoModel, Integer> {
}
