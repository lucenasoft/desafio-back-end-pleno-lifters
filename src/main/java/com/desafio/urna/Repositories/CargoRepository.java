package com.desafio.urna.Repositories;

import com.desafio.urna.Models.CargoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CargoRepository extends JpaRepository<CargoModel, Long> {
    @Query("SELECT e FROM cargo e WHERE e.nome = ?1")
    CargoModel findByNome(String nome);
}
