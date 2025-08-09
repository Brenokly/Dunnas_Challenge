package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.pedido;

import java.math.BigDecimal;
import java.util.UUID;

public record ItemPedidoResponseDTO(
    UUID produtoId,
    String nomeProduto,
    Integer quantidade,
    BigDecimal precoUnitarioCobrado) {
}