package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.pedido;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.mapping.List;

public record PedidoResponseDTO(
    UUID id,
    UUID clienteId,
    List<ItemPedidoResponseDTO> itens,
    String codigoCupom,
    BigDecimal valorTotal,
    LocalDate dataCriacao) {
}
