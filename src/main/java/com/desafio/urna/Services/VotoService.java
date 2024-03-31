package com.desafio.urna.Services;

import com.desafio.urna.Models.VotoModel;
import com.desafio.urna.Repositories.VotoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VotoService {
    @Autowired
    private VotoRepository votoRepository;

    public Long countVotosByCandidatoId(Long candidatoId) {
        return votoRepository.countVotosByCandidatoId(candidatoId);
    }

    @Transactional
    public void saveVoto(VotoModel voto) {
        votoRepository.save(voto);
    }
}
