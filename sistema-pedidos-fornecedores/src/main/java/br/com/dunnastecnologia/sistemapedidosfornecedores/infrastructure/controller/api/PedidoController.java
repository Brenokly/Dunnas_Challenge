package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.controller.api;

import java.net.URI;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases.PedidoUseCases;
import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.utils.enums.StatusPedido;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.pedido.PedidoFornecedorDetalhadoResponseDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.pedido.PedidoRequestDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.pedido.PedidoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/pedidos")
@Tag(name = "Pedidos", description = "Endpoints para a realização e consulta de pedidos.")
public class PedidoController {

  private final PedidoUseCases pedidoUseCases;

  public PedidoController(PedidoUseCases pedidoUseCases) {
    this.pedidoUseCases = pedidoUseCases;
  }

  @PostMapping
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Cliente cria um novo pedido", description = "Realiza a transação completa de um novo pedido. Requer autenticação de Cliente.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Pedido realizado com sucesso."),
      @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou regra de negócio violada (ex: saldo insuficiente, cupom inválido)."),
      @ApiResponse(responseCode = "403", description = "Acesso negado (usuário não é um cliente).")
  })
  public ResponseEntity<PedidoResponseDTO> criar(@RequestBody @Valid PedidoRequestDTO requestDTO,
      Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    PedidoResponseDTO responseDTO = pedidoUseCases.criarNovoPedido(requestDTO, userDetails);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(responseDTO.id()).toUri();
    return ResponseEntity.created(location).body(responseDTO);
  }

  @GetMapping
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Cliente lista seu histórico de pedidos", description = "Retorna uma lista paginada de todos os pedidos pertencentes ao cliente autenticado.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Histórico de pedidos retornado com sucesso."),
      @ApiResponse(responseCode = "403", description = "Acesso negado (usuário não é um cliente).")
  })
  public ResponseEntity<Page<PedidoResponseDTO>> listarMeusPedidos(
      @ParameterObject Pageable pageable,
      @RequestParam(required = false) StatusPedido status,
      Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    Page<PedidoResponseDTO> pedidos;
    if (status != null) {
      pedidos = pedidoUseCases.listarPedidosDoClientePorStatus(userDetails, status, pageable);
    } else {
      pedidos = pedidoUseCases.listarPedidosDoCliente(userDetails, pageable);
    }
    return ResponseEntity.ok(pedidos);
  }

  @GetMapping("/{id}")
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Cliente busca um de seus pedidos por ID", description = "Retorna os detalhes de um pedido específico, validando se ele pertence ao cliente autenticado.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Pedido encontrado com sucesso."),
      @ApiResponse(responseCode = "403", description = "Acesso negado (pedido não pertence ao cliente)."),
      @ApiResponse(responseCode = "404", description = "Pedido não encontrado.")
  })
  public ResponseEntity<PedidoResponseDTO> buscarMeuPedidoPorId(@PathVariable UUID id, Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    PedidoResponseDTO pedido = pedidoUseCases.buscarPedidoPorId(id, userDetails);
    return ResponseEntity.ok(pedido);
  }

  @GetMapping("/fornecedor")
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Fornecedor lista seus pedidos", description = "Retorna uma lista paginada de pedidos feitos para o fornecedor autenticado.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de pedidos do fornecedor retornada com sucesso."),
      @ApiResponse(responseCode = "403", description = "Acesso negado (usuário não é um fornecedor).")
  })
  public ResponseEntity<Page<PedidoFornecedorDetalhadoResponseDTO>> listarPedidosFornecedor(
      @ParameterObject Pageable pageable, Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    Page<PedidoFornecedorDetalhadoResponseDTO> pedidos = pedidoUseCases.listarPedidosDoFornecedor(userDetails,
        pageable);
    return ResponseEntity.ok(pedidos);
  }
}