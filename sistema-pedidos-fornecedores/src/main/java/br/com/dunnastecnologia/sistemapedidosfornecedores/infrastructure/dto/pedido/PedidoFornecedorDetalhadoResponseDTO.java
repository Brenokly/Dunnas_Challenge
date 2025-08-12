package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.pedido;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record PedidoFornecedorDetalhadoResponseDTO(
        UUID id,
        OffsetDateTime dataPedido,
        String nomeCliente,
        String status,
        List<ItemPedidoResponseDTO> itens) {
}