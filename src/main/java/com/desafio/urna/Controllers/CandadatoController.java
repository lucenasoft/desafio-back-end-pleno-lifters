package com.desafio.urna.Controllers;
import com.desafio.urna.Dtos.CandidatoDTO;
import com.desafio.urna.Models.CandidatoModel;
import com.desafio.urna.Services.CandidatoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Candidatos", description = "API para gerenciamento de candidatos")
@CrossOrigin(origins = "*")
public class CandadatoController {
    @Autowired
    private CandidatoService candidatoService;

    @Operation(summary = "Retorna todos os candidatos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidatos retornados com sucesso",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Nenhum candidato encontrado",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/")
    public ResponseEntity<List<CandidatoModel>> getAllCandidatos() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(candidatoService.getAllCandidatos());
    }

    @Operation(summary = "Retorna um candidato pelo id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidato retornado com sucesso",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Candidato não encontrado",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getCandidatoById(Integer id) {
        Optional<CandidatoModel> candidato = candidatoService.getCandidatoById(id);
        if (candidato.isEmpty()) return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Candidato não encontrado");
        return ResponseEntity.status(HttpStatus.OK).body(candidato);
    }

    @Operation(summary = "Cria um novo candidato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Candidato criado com sucesso",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Erro na requisição",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "Candidato já existe",
                    content = @Content(mediaType = "application/json"))
    })
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

    @Operation(summary = "Atualiza um candidato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidato atualizado com sucesso",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Erro na requisição",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Candidato não encontrado",
                    content = @Content(mediaType = "application/json"))
    })
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

    @Operation(summary = "Deleta um candidato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidato deletado com sucesso",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Candidato não encontrado",
                    content = @Content(mediaType = "application/json"))
    })
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
