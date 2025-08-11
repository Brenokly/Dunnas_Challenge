package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.controller.api;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases.CategoriaUseCases;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.categoria.CategoriaRequestDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.categoria.CategoriaResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/categorias")
@Tag(name = "Categorias", description = "Endpoints para a visualização e gerenciamento de categorias de produtos.")
public class CategoriaController {

  private final CategoriaUseCases categoriaUseCases;

  public CategoriaController(CategoriaUseCases categoriaUseCases) {
    this.categoriaUseCases = categoriaUseCases;
  }

  // ENDPOINTS PÚBLICOS

  @GetMapping
  @Operation(summary = "Lista todas as categorias ativas", description = "Retorna uma lista paginada de todas as categorias ativas no sistema.")
  @ApiResponse(responseCode = "200", description = "Lista de categorias retornada com sucesso.")
  public ResponseEntity<Page<CategoriaResponseDTO>> listarAtivas(@ParameterObject Pageable pageable) {
    return ResponseEntity.ok(categoriaUseCases.listarCategoriasAtivas(pageable));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Busca uma categoria ativa por ID", description = "Retorna os detalhes de uma categoria ativa específica.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Categoria encontrada."),
      @ApiResponse(responseCode = "404", description = "Categoria não encontrada ou inativa.")
  })
  public ResponseEntity<CategoriaResponseDTO> buscarAtivaPorId(@PathVariable UUID id) {
    return ResponseEntity.ok(categoriaUseCases.buscarCategoriaAtivaPorId(id));
  }

  // ENDPOINTS PRIVADOS (ADMINISTRATIVOS)

  @PostMapping
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Cria uma nova categoria", description = "Cria uma nova categoria de produto. Requer autenticação (ex: Fornecedor).")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso."),
      @ApiResponse(responseCode = "403", description = "Acesso negado."),
      @ApiResponse(responseCode = "409", description = "Nome de categoria já existente.")
  })
  public ResponseEntity<CategoriaResponseDTO> criar(@RequestBody @Valid CategoriaRequestDTO requestDTO,
      Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    CategoriaResponseDTO responseDTO = categoriaUseCases.criarNovaCategoria(requestDTO, userDetails);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(responseDTO.id()).toUri();
    return ResponseEntity.created(location).body(responseDTO);
  }

  @PutMapping("/{id}")
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Atualiza o nome de uma categoria", description = "Atualiza o nome de uma categoria existente. Requer autenticação.")
  public ResponseEntity<CategoriaResponseDTO> atualizar(@PathVariable UUID id,
      @RequestBody @Valid CategoriaRequestDTO requestDTO, Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    CategoriaResponseDTO responseDTO = categoriaUseCases.atualizarCategoria(id, requestDTO, userDetails);
    return ResponseEntity.ok(responseDTO);
  }

  @DeleteMapping("/{id}")
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Desativa uma categoria", description = "Desativa uma categoria. Requer autenticação.")
  public ResponseEntity<Void> desativar(@PathVariable UUID id, Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    categoriaUseCases.desativarCategoria(id, userDetails);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{id}/reativar")
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Reativa uma categoria", description = "Reativa uma categoria. Requer autenticação.")
  public ResponseEntity<CategoriaResponseDTO> reativar(@PathVariable UUID id, Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    return ResponseEntity.ok(categoriaUseCases.reativarCategoria(id, userDetails));
  }
}