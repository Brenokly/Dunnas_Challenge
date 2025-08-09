package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.pedido;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemPedidoRequestDTO(
    @NotNull UUID produtoId,

    @NotNull @Positive(message = "A quantidade deve ser um número positivo.") Integer quantidade) {
}
