package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.pedido;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;

public record PedidoRequestDTO(
    @NotEmpty List<ItemPedidoRequestDTO> itens,
    String codigoCupom) {
}
