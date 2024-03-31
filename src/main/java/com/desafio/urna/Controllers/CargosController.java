package com.desafio.urna.Controllers;

import com.desafio.urna.Dtos.CargoDTO;
import com.desafio.urna.Errors.ErrorResponse;
import com.desafio.urna.Models.CargoModel;
import com.desafio.urna.Services.CargoService;
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
@RequestMapping("/cargos")
@Tag(name = "Cargos", description = "Endpoints para gerenciamento de cargos")
@CrossOrigin(origins = "*")
public class CargosController {
    @Autowired
    private CargoService cargoService;

    @Operation(summary = "Listar todos os cargos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cargos listados com sucesso",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(mediaType = "application/json"))
    })
    @Parameter(name = "page", description = "Número da página")
    @Parameter(name = "size", description = "Número de elementos por página")
    @GetMapping("/")
    public ResponseEntity<Page<CargoModel>> buscarCargos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK)
                .body(cargoService.listarCargos(pageable));
    }

    @Operation(summary = "Buscar cargo por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cargo encontrado com sucesso",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Cargo não encontrado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(mediaType = "application/json"))
    })
    @Parameter(name = "id", description = "Id do cargo")
    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarCargoPorId(@PathVariable Long id) {
        Optional<CargoModel> cargo = cargoService.getCargoById(id);
        if (cargo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "Cargo não encontrado"));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(cargo);
    }

    @Operation(summary = "Cadastrar um novo cargo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cargo cadastrado com sucesso",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<Object> cadastrarCargo(
            @RequestBody @Valid CargoDTO cargoDTO,
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

        if (cargoService.getCargoByNome(cargoDTO.getNome()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "Cargo já cadastrado"));
        }
        var cargo = new CargoModel();
        BeanUtils.copyProperties(cargoDTO, cargo);

        cargoService.cadastrarCargo(cargo);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap("message", "Cargo cadastrado com sucesso"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCargo(
            @PathVariable Long id,
            @RequestBody @Valid CargoDTO cargoDTO,
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

        if (cargoService.getCargoById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "Cargo não encontrado"));
        }

        if (cargoService.getCargoByNome(cargoDTO.getNome()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "Cargo já cadastrado"));
        }

        var cargo = new CargoModel();
        BeanUtils.copyProperties(cargoDTO, cargo);

        cargoService.atualizarCargo(id, cargo);

        return ResponseEntity.status(HttpStatus.OK)
                .body(Collections.singletonMap("message", "Cargo atualizado com sucesso"));
    }

    @Operation(summary = "Deleta um cargo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cargo deletado com sucesso",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Cargo não encontrado",
                    content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarCargo(@PathVariable Long id) {
        Optional<CargoModel> cargo = cargoService.getCargoById(id);
        if (cargo.isEmpty()) return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("message", "Candidato não encontrado"));

        cargoService.deletarCargo(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(Collections.singletonMap("message", "Cargo deletado com sucesso!"));
    }
}
