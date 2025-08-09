package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.pedido;

import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.utils.enums.StatusPedido;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record PedidoResponseDTO(
        UUID id,
        OffsetDateTime dataPedido,
        BigDecimal valorBruto,
        BigDecimal valorDesconto,
        BigDecimal valorFinal,
        StatusPedido status,
        UUID clienteId,
        String codigoCupom,
        List<ItemPedidoResponseDTO> itens) {
}