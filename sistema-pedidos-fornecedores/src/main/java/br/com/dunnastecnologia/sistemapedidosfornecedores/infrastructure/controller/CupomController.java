package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.controller;

import java.net.URI;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@Tag(name = "Cupons", description = "Endpoints para o gerenciamento de cupons de desconto por fornecedores.")
public class CupomController {

  private final CupomUseCases cupomUseCases;

  public CupomController(CupomUseCases cupomUseCases) {
    this.cupomUseCases = cupomUseCases;
  }

  @PostMapping
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Fornecedor cria um novo cupom", description = "Cria um novo cupom de desconto associado ao fornecedor autenticado. Requer autenticação de Fornecedor.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Cupom criado com sucesso."),
      @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos."),
      @ApiResponse(responseCode = "403", description = "Acesso negado (usuário não é um fornecedor)."),
      @ApiResponse(responseCode = "409", description = "Código de cupom já existente.")
  })
  public ResponseEntity<CupomResponseDTO> criar(@RequestBody @Valid CupomRequestDTO requestDTO,
      Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    CupomResponseDTO responseDTO = cupomUseCases.criarNovoCupom(requestDTO, userDetails);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(responseDTO.id()).toUri();
    return ResponseEntity.created(location).body(responseDTO);
  }

  @GetMapping
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Fornecedor lista seus próprios cupons", description = "Retorna uma lista paginada de todos os cupons (ativos e inativos) pertencentes ao fornecedor autenticado.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de cupons retornada com sucesso."),
      @ApiResponse(responseCode = "403", description = "Acesso negado (usuário não é um fornecedor).")
  })
  public ResponseEntity<Page<CupomResponseDTO>> listarMeusCupons(@ParameterObject Pageable pageable,
      Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    Page<CupomResponseDTO> cupons = cupomUseCases.listarCuponsDoFornecedor(userDetails, pageable);
    return ResponseEntity.ok(cupons);
  }

  @GetMapping("/{id}")
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Fornecedor busca um de seus cupons por ID", description = "Retorna os detalhes de um cupom específico, validando se ele pertence ao fornecedor autenticado.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Cupom encontrado com sucesso."),
      @ApiResponse(responseCode = "403", description = "Acesso negado (cupom não pertence ao fornecedor)."),
      @ApiResponse(responseCode = "404", description = "Cupom não encontrado.")
  })
  public ResponseEntity<CupomResponseDTO> buscarPorId(@PathVariable UUID id, Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    CupomResponseDTO cupom = cupomUseCases.buscarPorIdDoFornecedor(id, userDetails);
    return ResponseEntity.ok(cupom);
  }

  @DeleteMapping("/{id}")
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Fornecedor desativa um de seus cupons", description = "Altera o status de um cupom para 'inativo', validando a propriedade.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Cupom desativado com sucesso."),
      @ApiResponse(responseCode = "403", description = "Acesso negado."),
      @ApiResponse(responseCode = "404", description = "Cupom não encontrado.")
  })
  public ResponseEntity<Void> desativar(@PathVariable UUID id, Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    cupomUseCases.desativarCupom(id, userDetails);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{id}/reativar")
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Fornecedor reativa um de seus cupons", description = "Altera o status de um cupom para 'ativo', validando a propriedade.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Cupom reativado com sucesso."),
      @ApiResponse(responseCode = "403", description = "Acesso negado."),
      @ApiResponse(responseCode = "404", description = "Cupom não encontrado.")
  })
  public ResponseEntity<CupomResponseDTO> reativar(@PathVariable UUID id, Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    CupomResponseDTO cupomReativado = cupomUseCases.reativarCupom(id, userDetails);
    return ResponseEntity.ok(cupomReativado);
  }
}