package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.cliente.ClienteRequestDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.cliente.ClienteResponseDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.cliente.ValorRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @GetMapping("/meu-perfil")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Busca os dados do cliente autenticado", description = "Retorna os detalhes do cliente autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados do cliente retornados."),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado.")
    })
    public ResponseEntity<ClienteResponseDTO> buscarMeuPerfil(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(clienteUseCases.buscarClienteLogado(userDetails));
    }

    @PatchMapping("/meu-perfil/saldo")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Adiciona saldo à conta do cliente autenticado", description = "Realiza uma operação de adição de saldo na conta do cliente autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Saldo adicionado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Valor inválido."),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado.")
    })
    public ResponseEntity<ClienteResponseDTO> adicionarSaldo(@RequestBody @Valid ValorRequestDTO valorDTO,
            Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(clienteUseCases.adicionarSaldo(userDetails, valorDTO));
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Desativa a conta do cliente autenticado", description = "Altera o status da conta do cliente autenticado para 'inativo'.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente desativado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado.")
    })
    public ResponseEntity<Void> desativar(@PathVariable UUID id, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        clienteUseCases.desativarCliente(id, userDetails);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/reativar")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Reativa a conta do cliente autenticado", description = "Altera o status da conta do cliente autenticado para 'ativo'.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente reativado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado.")
    })
    public ResponseEntity<ClienteResponseDTO> reativar(@PathVariable UUID id, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        ClienteResponseDTO clienteReativado = clienteUseCases.reativarCliente(id, userDetails);
        return ResponseEntity.ok(clienteReativado);
    }
}
