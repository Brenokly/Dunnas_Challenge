package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.controller;

import java.net.URI;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases.FornecedorUseCases;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.fornecedor.FornecedorRequestDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.fornecedor.FornecedorResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/fornecedores")
@Tag(name = "Fornecedores", description = "Endpoints para o gerenciamento de fornecedores.")
public class FornecedorController {

    private final FornecedorUseCases fornecedorUseCases;

    public FornecedorController(FornecedorUseCases fornecedorUseCases) {
        this.fornecedorUseCases = fornecedorUseCases;
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo fornecedor", description = "Cria um novo fornecedor no sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Fornecedor cadastrado com sucesso."),
        @ApiResponse(responseCode = "409", description = "CNPJ ou usuário já cadastrado.")
    })
    public ResponseEntity<FornecedorResponseDTO> cadastrar(@RequestBody @Valid FornecedorRequestDTO requestDTO) {
        FornecedorResponseDTO responseDTO = fornecedorUseCases.cadastrarNovoFornecedor(requestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDTO.id())
                .toUri();
        return ResponseEntity.created(location).body(responseDTO);
    }

    @GetMapping
    @Operation(summary = "Lista todos os fornecedores ativos", description = "Retorna uma lista paginada de todos os fornecedores ativos.")
    @ApiResponse(responseCode = "200", description = "Lista de fornecedores retornada com sucesso.")
    public ResponseEntity<Page<FornecedorResponseDTO>> listar(@ParameterObject Pageable pageable) {
        Page<FornecedorResponseDTO> fornecedores = fornecedorUseCases.listarTodos(pageable);
        return ResponseEntity.ok(fornecedores);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um fornecedor por ID", description = "Retorna os detalhes de um fornecedor ativo específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Fornecedor encontrado com sucesso."),
        @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado.")
    })
    public ResponseEntity<FornecedorResponseDTO> buscarPorId(@PathVariable UUID id) {
        FornecedorResponseDTO fornecedor = fornecedorUseCases.buscarPorId(id);
        return ResponseEntity.ok(fornecedor);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativa um fornecedor", description = "Desativa um fornecedor pelo seu ID (soft delete).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Fornecedor desativado com sucesso."),
        @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado.")
    })
    public ResponseEntity<Void> desativar(@PathVariable UUID id) {
        fornecedorUseCases.desativarFornecedor(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/reativar")
    @Operation(summary = "Reativa um fornecedor desativado", description = "Altera o status de um fornecedor para 'ativo'.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Fornecedor reativado com sucesso."),
        @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado.")
    })
    public ResponseEntity<FornecedorResponseDTO> reativar(@PathVariable UUID id) {
        FornecedorResponseDTO fornecedorReativado = fornecedorUseCases.reativarFornecedor(id);
        return ResponseEntity.ok(fornecedorReativado);
    }
}
