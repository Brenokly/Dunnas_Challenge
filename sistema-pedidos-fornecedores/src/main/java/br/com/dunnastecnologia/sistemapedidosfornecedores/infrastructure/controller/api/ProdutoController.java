package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.controller.api;

import java.net.URI;
import java.util.Set;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases.ProdutoUseCases;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.produto.ProdutoRequestDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.produto.ProdutoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/produtos")
@Tag(name = "Produtos", description = "Endpoints para gerenciamento e visualização de produtos.")
public class ProdutoController {

  private final ProdutoUseCases produtoUseCases;

  public ProdutoController(ProdutoUseCases produtoUseCases) {
    this.produtoUseCases = produtoUseCases;
  }

  // ENDPOINTS PÚBLICOS (PARA CLIENTES E VISITANTES)

  @GetMapping
  @Operation(summary = "Lista todos os produtos ativos (visão pública)", description = "Retorna uma lista paginada de produtos. Pode ser filtrada por uma ou mais categorias via query param `categorias`.")
  @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso.")
  public ResponseEntity<Page<ProdutoResponseDTO>> listarPublico(
      @RequestParam(required = false) Set<UUID> categorias,
      @ParameterObject Pageable pageable) {
    return ResponseEntity.ok(produtoUseCases.listarProdutosPublicos(categorias, pageable));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Busca um produto ativo por ID (visão pública)")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Produto encontrado."),
      @ApiResponse(responseCode = "404", description = "Produto não encontrado ou inativo.")
  })
  public ResponseEntity<ProdutoResponseDTO> buscarPublicoPorId(@PathVariable UUID id) {
    return ResponseEntity.ok(produtoUseCases.buscarProdutoPublicoPorId(id));
  }

  // ENDPOINTS PRIVADOS (PARA FORNECEDORES GERENCIAREM SEU CATÁLOGO)

  @PostMapping
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Fornecedor cadastra um novo produto", description = "Requer autenticação de Fornecedor.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Produto cadastrado com sucesso."),
      @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos."),
      @ApiResponse(responseCode = "403", description = "Acesso negado (usuário não é um fornecedor).")
  })
  public ResponseEntity<ProdutoResponseDTO> cadastrar(@RequestBody @Valid ProdutoRequestDTO dto,
      Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    ProdutoResponseDTO responseDTO = produtoUseCases.cadastrarNovoProduto(dto, userDetails);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(responseDTO.id())
        .toUri();
    return ResponseEntity.created(location).body(responseDTO);
  }

  @GetMapping("/meus-produtos")
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Fornecedor lista todos os seus produtos", description = "Retorna produtos ativos e inativos do fornecedor autenticado. Requer autenticação de Fornecedor.")
  public ResponseEntity<Page<ProdutoResponseDTO>> listarMeusProdutos(@ParameterObject Pageable pageable,
      Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    Page<ProdutoResponseDTO> produtos = produtoUseCases.listarProdutosDoFornecedorLogado(userDetails, pageable);
    return ResponseEntity.ok(produtos);
  }

  @PutMapping("/{id}")
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Fornecedor atualiza um de seus produtos", description = "Requer autenticação e propriedade do produto.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso."),
      @ApiResponse(responseCode = "403", description = "Acesso negado."),
      @ApiResponse(responseCode = "404", description = "Produto não encontrado.")
  })
  public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable UUID id, @RequestBody @Valid ProdutoRequestDTO dto,
      Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    ProdutoResponseDTO responseDTO = produtoUseCases.atualizarProduto(id, dto, userDetails);
    return ResponseEntity.ok(responseDTO);
  }

  @DeleteMapping("/{id}")
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Fornecedor desativa um de seus produtos", description = "Requer autenticação e propriedade do produto.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Produto desativado com sucesso."),
      @ApiResponse(responseCode = "403", description = "Acesso negado."),
      @ApiResponse(responseCode = "404", description = "Produto não encontrado.")
  })
  public ResponseEntity<Void> desativar(@PathVariable UUID id, Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    produtoUseCases.desativarProduto(id, userDetails);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{id}/reativar")
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Fornecedor reativa um de seus produtos", description = "Requer autenticação e propriedade do produto.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Produto reativado com sucesso."),
      @ApiResponse(responseCode = "403", description = "Acesso negado."),
      @ApiResponse(responseCode = "404", description = "Produto não encontrado.")
  })
  public ResponseEntity<ProdutoResponseDTO> reativar(@PathVariable UUID id, Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    ProdutoResponseDTO responseDTO = produtoUseCases.reativarProduto(id, userDetails);
    return ResponseEntity.ok(responseDTO);
  }
}