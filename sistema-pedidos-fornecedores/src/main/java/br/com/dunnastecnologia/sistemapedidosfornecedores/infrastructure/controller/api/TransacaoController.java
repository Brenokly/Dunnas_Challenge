package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.controller.api;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases.TransacaoUseCases;
import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.utils.enums.TipoTransacao;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.transacao.TransacaoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/transacoes")
@Tag(name = "Transações", description = "Endpoints para a visualização do histórico de transações financeiras.")
public class TransacaoController {

    private final TransacaoUseCases transacaoUseCases;

    public TransacaoController(TransacaoUseCases transacaoUseCases) {
        this.transacaoUseCases = transacaoUseCases;
    }

    @GetMapping("/meu-historico")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Cliente lista seu histórico de transações", description = "Retorna o extrato de transações financeiras (pagamentos, adições de saldo) do cliente autenticado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Histórico de transações retornado com sucesso."),
        @ApiResponse(responseCode = "403", description = "Acesso negado (usuário não é um cliente).")
    })
    public ResponseEntity<Page<TransacaoResponseDTO>> listarMinhasTransacoes(
            @ParameterObject Pageable pageable,
            @RequestParam(required = false) TipoTransacao tipo,
            Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Page<TransacaoResponseDTO> transacoes;
        if (tipo != null) {
            transacoes = transacaoUseCases.listarTransacoesDoClientePorTipo(userDetails, tipo, pageable);
        } else {
            transacoes = transacaoUseCases.listarTransacoesDoCliente(userDetails, pageable);
        }
        return ResponseEntity.ok(transacoes);
    }
}
