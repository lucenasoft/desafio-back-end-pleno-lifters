package com.desafio.urna.Controllers;

import com.desafio.urna.Dtos.EleitorDTO;
import com.desafio.urna.Errors.ErrorResponse;
import com.desafio.urna.Models.EleitorModel;
import com.desafio.urna.Services.EleitorService;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/eleitores")
@Tag(name = "Eleitores", description = "Endpoints para gerenciamento de eleitores")
@CrossOrigin(origins = "*")
public class EleitorController {
    @Autowired
    private EleitorService eleitorService;

    @Operation(summary = "Retorna todos os eleitores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Eleitores retornados com sucesso",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Nenhum eleitor encontrado",
                    content = @Content(mediaType = "application/json"))
    })
    @Parameter(name = "page", description = "Número da página")
    @Parameter(name = "size", description = "Número de elementos por página")
    @GetMapping("/")
    public ResponseEntity<Page<EleitorModel>> getAllEleitores(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK)
                .body(eleitorService.getAllEleitores(pageable));
    }

    @Operation(summary = "Retorna um eleitor pelo id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Eleitor retornado com sucesso",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Eleitor não encontrado",
                    content = @Content(mediaType = "application/json"))
    })
    @Parameter(name = "id", description = "Id do eleitor", required = true)
    @GetMapping("/{id}")
    public ResponseEntity<Object> getEleitorById(@PathVariable Long id) {
        Optional<EleitorModel> eleitor = eleitorService.getEleitorById(id);
        if (eleitor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "Eleitor não encontrado"));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(eleitor);
    }

    @Operation(summary = "Cadastra um novo eleitor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Eleitor cadastrado com sucesso",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Erro ao cadastrar eleitor",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/")
    public ResponseEntity<Object> createEleitor(
            @RequestBody @Valid EleitorDTO eleitorDTO,
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

        if (eleitorService.getEleitorByNome(eleitorDTO.getNome()) != null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Collections.singletonMap("message", "Eleitor já existe"));
        }
        var eleitor = new EleitorModel();
        BeanUtils.copyProperties(eleitorDTO, eleitor);

        eleitorService.saveEleitor(eleitor);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap("message", "Eleitor cadastrado com sucesso"));
    }

    @Operation(summary = "Atualiza um eleitor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Eleitor atualizado com sucesso",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Eleitor não encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Erro ao atualizar eleitor",
                    content = @Content(mediaType = "application/json"))
    })
    @Parameter(name = "id", description = "Id do eleitor", required = true)
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateEleitor(
            @PathVariable Long id,
            @RequestBody @Valid EleitorDTO eleitorDTO,
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

        Optional<EleitorModel> eleitor = eleitorService.getEleitorById(id);
        if (eleitor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "Eleitor não encontrado"));
        }

        if (eleitorService.getEleitorByNome(eleitorDTO.getNome()) != null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Collections.singletonMap("message", "Nome de eleitor já existe"));
        }

        BeanUtils.copyProperties(eleitorDTO, eleitor.get());
        eleitorService.saveEleitor(eleitor.get());

        return ResponseEntity.status(HttpStatus.OK)
                .body(Collections.singletonMap("message", "Eleitor atualizado com sucesso"));
    }

    @Operation(summary = "Deleta um eleitor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Eleitor deletado com sucesso",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Eleitor não encontrado",
                    content = @Content(mediaType = "application/json"))
    })
    @Parameter(name = "id", description = "Id do eleitor", required = true)
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteEleitor(@PathVariable Long id) {
        Optional<EleitorModel> eleitor = eleitorService.getEleitorById(id);
        if (eleitor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "Eleitor não encontrado"));
        }
        eleitorService.deleteEleitor(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Collections.singletonMap("message", "Eleitor deletado com sucesso"));
    }
}
