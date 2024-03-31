package com.desafio.urna.Controllers;
import com.desafio.urna.Dtos.CandidatoDTO;
import com.desafio.urna.Errors.ErrorResponse;
import com.desafio.urna.Models.CandidatoModel;
import com.desafio.urna.Services.CandidatoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/candidatos")
@Tag(name = "Candidatos", description = "Endpoints para gerenciamento de candidatos")
@CrossOrigin(origins = "*")
public class CandadatoController {
    @Autowired
    private CandidatoService candidatoService;

    @Operation(summary = "Retorna todos os candidatos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidatos retornados com sucesso",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Nenhum candidato encontrado",
                    content = @Content(mediaType = "application/json"))
    })
    @Parameter(name = "page", description = "Número da página")
    @Parameter(name = "size", description = "Número de elementos por página")
    @GetMapping("/")
    public ResponseEntity<Page<CandidatoModel>> getAllCandidatos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK)
                .body(candidatoService.getAllCandidatos(pageable));
    }

    @Operation(summary = "Retorna um candidato pelo id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidato retornado com sucesso",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Candidato não encontrado",
                    content = @Content(mediaType = "application/json"))
    })
    @Parameter(name = "id", description = "Id do candidato", required = true)
    @GetMapping("/{id}")
    public ResponseEntity<Object> getCandidatoById(@PathVariable Long id) {
        Optional<CandidatoModel> candidato = candidatoService.getCandidatoById(id);
        if (candidato.isEmpty()) return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("message", "Candidato não encontrado"));
        return ResponseEntity.status(HttpStatus.OK).body(candidato);
    }

    @Operation(summary = "Cria um novo candidato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Candidato criado com sucesso",
                    content = @Content(mediaType = "application/json")),
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

        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.add(error.getField() + ": " + error.getDefaultMessage());
            }
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                    "Validation error", System.currentTimeMillis());
            errorResponse.setErrors(errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        if (candidatoService.getCandidatoByNome(candidatoDTO.getNome()) != null)
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap("message", "Nome de candidato já existe"));

        var candidato = new CandidatoModel();
        BeanUtils.copyProperties(candidatoDTO, candidato);

        candidatoService.saveCandidato(candidato);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap("message", "Candidato criado com sucesso"));
    }

    @Operation(summary = "Atualiza um candidato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidato atualizado com sucesso",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Erro na requisição",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Candidato não encontrado",
                    content = @Content(mediaType = "application/json"))
    })
    @Parameter(name = "id", description = "Id do candidato", required = true)
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCandidato(
            @PathVariable(value = "id") Long id,
            @RequestBody @Valid CandidatoDTO candidatoDTO,
            BindingResult bindingResult
            ) {
        Optional<CandidatoModel> candidatoExistente = candidatoService.getCandidatoById(id);
        if (candidatoExistente.isEmpty()) return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("message", "Candidato não encontrado"));

        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.add(error.getField() + ": " + error.getDefaultMessage());
            }
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                    "Validation error", System.currentTimeMillis());
            errorResponse.setErrors(errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        if (candidatoService.getCandidatoByNome(candidatoDTO.getNome()) != null) return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap("message", "Nome de candidato já existe"));

        CandidatoModel candidato = new CandidatoModel();
        BeanUtils.copyProperties(candidatoDTO, candidato);

        candidatoService.updateCandidato(id, candidato);

        return ResponseEntity.status(HttpStatus.OK).body(
                Collections.singletonMap("message", "Candidato atualizado com sucesso")
        );
    }

    @Operation(summary = "Deleta um candidato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidato deletado com sucesso",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Candidato não encontrado",
                    content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCandidato(@PathVariable Long id) {
        Optional<CandidatoModel> candidato = candidatoService.getCandidatoById(id);
        if (candidato.isEmpty()) return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("message", "Candidato não encontrado"));

        //if (candidato.get().getVotos() > 0) return ResponseEntity
        //        .status(HttpStatus.CONFLICT)
        //        .body(Collections.singletonMap("message", "Candidato possui votos registrados"));

        candidatoService.deleteCandidato(id);

        return ResponseEntity.status(HttpStatus.OK).body(
                Collections.singletonMap("message", "Candidato deletado com sucesso")
        );
    }
}
