package com.desafio.urna.Services;

import com.desafio.urna.Models.EleitorModel;
import com.desafio.urna.Repositories.EleitorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EleitorService {
    @Autowired
    private EleitorRepository eleitorRepository;

    public Page<EleitorModel> getAllEleitores(Pageable pageable) {
        return eleitorRepository.findAll(pageable);
    }

    public Optional<EleitorModel> getEleitorById(Long id) {
        return eleitorRepository.findById(id);
    }

    public EleitorModel getEleitorByNome(String nome) {
        return eleitorRepository.findByName(nome);
    }

    @Transactional
    public void saveEleitor(EleitorModel eleitor) {
        eleitorRepository.save(eleitor);
    }

    @Transactional
    public void updateEleitor(Long id, EleitorModel eleitor) {
        eleitor.setId(id);
        eleitorRepository.save(eleitor);
    }

    @Transactional
    public void deleteEleitor(Long id) {
        eleitorRepository.deleteById(id);
    }
}
