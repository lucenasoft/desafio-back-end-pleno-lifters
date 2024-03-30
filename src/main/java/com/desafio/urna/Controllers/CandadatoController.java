package com.desafio.urna.Controllers;
import com.desafio.urna.Dtos.CandidatoDTO;
import com.desafio.urna.Models.CandidatoModel;
import com.desafio.urna.Services.CandidatoService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/candidatos")
public class CandadatoController {
    @Autowired
    private CandidatoService candidatoService;

    @GetMapping("/")
    public ResponseEntity<List<CandidatoModel>> getAllCandidatos() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(candidatoService.getAllCandidatos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCandidatoById(Integer id) {
        Optional<CandidatoModel> candidato = candidatoService.getCandidatoById(id);
        if (candidato.isEmpty()) return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Candidato não encontrado");
        return ResponseEntity.status(HttpStatus.OK).body(candidato);
    }

    @PostMapping("/")
    public ResponseEntity<Object> createCandidato(
            @RequestBody @Valid CandidatoDTO candidatoDTO,
            BindingResult bindingResult
            ) {

        if (bindingResult.hasErrors()) return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());

        Optional<CandidatoModel> candidatoExistente = candidatoService.getCandidatoById(candidatoDTO.getNumero());

        if (candidatoExistente.isPresent()) return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(candidatoExistente.get());
        var candidato = new CandidatoModel();
        BeanUtils.copyProperties(candidatoDTO, candidato);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(candidatoService.saveCandidato(candidato));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCandidato(
            @PathVariable(value = "id") Integer id,
            @RequestBody @Valid CandidatoDTO candidatoDTO,
            BindingResult bindingResult
            ) {
        Optional<CandidatoModel> candidatoExistente = candidatoService.getCandidatoById(id);
        if (candidatoExistente.isEmpty()) return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Candidato não encontrado");

        if (bindingResult.hasErrors()) return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());

        CandidatoModel candidato = new CandidatoModel(
                candidatoDTO.getNome(),
                candidatoDTO.getPartido(),
                candidatoDTO.getNumero(),
                candidatoDTO.getVotos()
        );

        return ResponseEntity.status(HttpStatus.OK).body(candidatoService.updateCandidato(id, candidato));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCandidato(Integer id) {
        Optional<CandidatoModel> candidato = candidatoService.getCandidatoById(id);
        if (candidato.isEmpty()) return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Candidato não encontrado");
        candidatoService.deleteCandidato(id);
        return ResponseEntity.status(HttpStatus.OK).body("Candidato deletado com sucesso");
    }
}
