package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases.CupomUseCases;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.cupom.CupomRequestDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.cupom.CupomResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/cupons")
@Tag(name = "Cupons", description = "Endpoints para o gerenciamento de cupons de desconto.")
public class CupomController {
  private final CupomUseCases cupomUseCases;

  public CupomController(CupomUseCases cupomUseCases) {
    this.cupomUseCases = cupomUseCases;
  }

  @PostMapping
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Cria um novo cupom de desconto (Requer autenticação)")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Cupom criado com sucesso."),
      @ApiResponse(responseCode = "409", description = "Código de cupom já existente.")
  })
  public ResponseEntity<CupomResponseDTO> criar(@RequestBody @Valid CupomRequestDTO requestDTO) {
    CupomResponseDTO responseDTO = cupomUseCases.criarNovoCupom(requestDTO);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(responseDTO.id()).toUri();
    return ResponseEntity.created(location).body(responseDTO);
  }
}
