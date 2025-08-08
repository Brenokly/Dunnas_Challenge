package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.controller;

import java.net.URI;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases.ClienteUseCases;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.ClienteRequestDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.ClienteResponseDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.ValorRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/clientes")
@Tag(name = "Clientes", description = "Endpoints para o gerenciamento de clientes.")
public class ClienteController {

    private final ClienteUseCases clienteUseCases;

    public ClienteController(ClienteUseCases clienteUseCases) {
        this.clienteUseCases = clienteUseCases;
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo cliente", description = "Cria um novo cliente no sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cliente cadastrado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou regra de negócio violada.")
    })
    public ResponseEntity<ClienteResponseDTO> cadastrar(@RequestBody @Valid ClienteRequestDTO requestDTO) {
        ClienteResponseDTO responseDTO = clienteUseCases.cadastrarNovoCliente(requestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDTO.id())
                .toUri();
        return ResponseEntity.created(location).body(responseDTO);
    }

    @GetMapping
    @Operation(summary = "Lista todos os clientes", description = "Retorna uma lista paginada de todos os clientes.")
    @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso.")
    public ResponseEntity<Page<ClienteResponseDTO>> listar(@ParameterObject Pageable pageable) {
        Page<ClienteResponseDTO> clientes = clienteUseCases.listarTodos(pageable);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um cliente por ID", description = "Retorna os detalhes de um cliente específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso."),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado.")
    })
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable UUID id) {
        ClienteResponseDTO cliente = clienteUseCases.buscarPorId(id);
        return ResponseEntity.ok(cliente);
    }

    @PatchMapping("/{id}/saldo")
    @Operation(summary = "Adiciona saldo a um cliente", description = "Realiza uma operação de adição de saldo na conta de um cliente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Saldo adicionado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Valor inválido."),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado.")
    })
    public ResponseEntity<ClienteResponseDTO> adicionarSaldo(@PathVariable UUID id, @RequestBody @Valid ValorRequestDTO valorDTO) {
        ClienteResponseDTO clienteAtualizado = clienteUseCases.adicionarSaldo(id, valorDTO);
        return ResponseEntity.ok(clienteAtualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um cliente", description = "Remove um cliente do sistema pelo seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cliente deletado com sucesso."),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado.")
    })
    public ResponseEntity<Void> desativar(@PathVariable UUID id) {
        clienteUseCases.desativarCliente(id);
        return ResponseEntity.noContent().build();
    }
}
