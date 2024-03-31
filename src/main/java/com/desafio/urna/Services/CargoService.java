package com.desafio.urna.Services;

import com.desafio.urna.Models.CargoModel;
import com.desafio.urna.Repositories.CargoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CargoService {
    @Autowired
    private CargoRepository cargoRepository;

    public Page<CargoModel> listarCargos(Pageable pageable) {
        return cargoRepository.findAll(pageable);
    }

    public Optional<CargoModel> getCargoById(Long id) {
        return cargoRepository.findById(id);
    }

    public CargoModel getCargoByNome(String nome) {
        return cargoRepository.findByNome(nome);
    }

    @Transactional
    public CargoModel cadastrarCargo(CargoModel cargo) {
        return cargoRepository.save(cargo);
    }

    @Transactional
    public void deletarCargo(Long id) {
        cargoRepository.deleteById(id);
    }

    @Transactional
    public void atualizarCargo(Long id, CargoModel cargo) {
        cargo.setId(id);
        cargoRepository.save(cargo);
    }
}
