package com.desafio.urna.Services;

import com.desafio.urna.Models.CandidatoModel;
import com.desafio.urna.Repositories.CandidatoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CandidatoService {
    @Autowired
    private CandidatoRepository candidatoRepository;

    public List<CandidatoModel> getAllCandidatos() {
        return candidatoRepository.findAll();
    }

    public Optional<CandidatoModel> getCandidatoById(int id) {
        return candidatoRepository.findById(id);
    }

    @Transactional
    public CandidatoModel saveCandidato(CandidatoModel candidato) {
        return candidatoRepository.save(candidato);
    }

    @Transactional
    public CandidatoModel updateCandidato(int id, CandidatoModel candidato) {
        candidato.setId(id);
        return candidatoRepository.save(candidato);
    }

    @Transactional
    public void deleteCandidato(int id) {
        candidatoRepository.deleteById(id);
    }
}
