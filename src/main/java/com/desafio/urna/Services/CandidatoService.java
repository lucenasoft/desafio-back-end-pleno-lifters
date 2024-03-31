package com.desafio.urna.Services;

import com.desafio.urna.Models.CandidatoModel;
import com.desafio.urna.Repositories.CandidatoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CandidatoService {
    @Autowired
    private CandidatoRepository candidatoRepository;

    public Page<CandidatoModel> getAllCandidatos(Pageable pageable) {
        return candidatoRepository.findAll(pageable);
    }

    public Optional<CandidatoModel> getCandidatoById(int id) {
        return candidatoRepository.findById(id);
    }

    public CandidatoModel getCandidatoByNome(String nome) {
        return candidatoRepository.findByName(nome);
    }

    @Transactional
    public void saveCandidato(CandidatoModel candidato) {
        candidatoRepository.save(candidato);
    }

    @Transactional
    public void updateCandidato(int id, CandidatoModel candidato) {
        candidato.setId(id);
        candidatoRepository.save(candidato);
    }

    @Transactional
    public void deleteCandidato(int id) {
        candidatoRepository.deleteById(id);
    }
}
