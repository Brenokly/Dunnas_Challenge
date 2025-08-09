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

import br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases.FornecedorUseCases;
import br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases.ProdutoUseCases;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.fornecedor.FornecedorRequestDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.fornecedor.FornecedorResponseDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.produto.ProdutoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/fornecedores")
@Tag(name = "Fornecedores", description = "Endpoints para o gerenciamento de fornecedores.")
public class FornecedorController {

    private final FornecedorUseCases fornecedorUseCases;
    private final ProdutoUseCases produtoUseCases;

    public FornecedorController(FornecedorUseCases fornecedorUseCases, ProdutoUseCases produtoUseCases) {
        this.fornecedorUseCases = fornecedorUseCases;
        this.produtoUseCases = produtoUseCases;
    }

    // ENDPOINTS PÚBLICOS

    @PostMapping
    @Operation(summary = "Cadastra um novo fornecedor (Público)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Fornecedor cadastrado com sucesso."),
            @ApiResponse(responseCode = "409", description = "CNPJ ou usuário já cadastrado.")
    })
    public ResponseEntity<FornecedorResponseDTO> cadastrar(@RequestBody @Valid FornecedorRequestDTO requestDTO) {
        FornecedorResponseDTO responseDTO = fornecedorUseCases.cadastrarNovoFornecedor(requestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(responseDTO.id()).toUri();
        return ResponseEntity.created(location).body(responseDTO);
    }

    @GetMapping
    @Operation(summary = "Lista todos os fornecedores ativos (Público)")
    public ResponseEntity<Page<FornecedorResponseDTO>> listar(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(fornecedorUseCases.listarTodos(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um fornecedor ativo por ID (Público)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fornecedor encontrado."),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado.")
    })
    public ResponseEntity<FornecedorResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(fornecedorUseCases.buscarPorId(id));
    }

    @GetMapping("/{fornecedorId}/produtos")
    @Operation(summary = "Lista os produtos ativos de um fornecedor específico (Público)")
    public ResponseEntity<Page<ProdutoResponseDTO>> listarProdutosPorFornecedor(
            @PathVariable UUID fornecedorId, @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(produtoUseCases.listarProdutosAtivosDeFornecedor(fornecedorId, pageable));
    }

    // ENDPOINTS PRIVADOS (REQUEREM AUTENTICAÇÃO E PROPRIEDADE)

    @GetMapping("/meu-perfil")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Busca os dados do fornecedor autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados do fornecedor retornados."),
            @ApiResponse(responseCode = "403", description = "Acesso negado (usuário não é um fornecedor)."),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado.")
    })
    public ResponseEntity<FornecedorResponseDTO> buscarMeuPerfil(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(fornecedorUseCases.buscarFornecedorLogado(userDetails));
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Desativa a conta do fornecedor (requer autenticação do próprio fornecedor)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Fornecedor desativado."),
            @ApiResponse(responseCode = "403", description = "Acesso negado."),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado.")
    })
    public ResponseEntity<Void> desativar(@PathVariable UUID id, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        fornecedorUseCases.desativarFornecedor(id, userDetails);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/reativar")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Reativa a conta do fornecedor (requer autenticação do próprio fornecedor)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fornecedor reativado."),
            @ApiResponse(responseCode = "403", description = "Acesso negado."),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado.")
    })
    public ResponseEntity<FornecedorResponseDTO> reativar(@PathVariable UUID id, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        FornecedorResponseDTO fornecedorReativado = fornecedorUseCases.reativarFornecedor(id, userDetails);
        return ResponseEntity.ok(fornecedorReativado);
    }
}