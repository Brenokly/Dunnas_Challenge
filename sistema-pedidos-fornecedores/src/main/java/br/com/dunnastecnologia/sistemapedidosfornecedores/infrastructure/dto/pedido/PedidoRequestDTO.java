package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.pedido;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record PedidoRequestDTO(
        @NotEmpty(message = "A lista de itens não pode ser vazia.") @Valid List<ItemPedidoRequestDTO> itens,
        String codigoCupom) {
}