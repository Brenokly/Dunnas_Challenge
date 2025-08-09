package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.produto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProdutoRequestDTO(
    @NotBlank String nome,
    String descricao,
    @NotNull @Positive BigDecimal preco,
    @NotNull @Min(0) @Max(100) BigDecimal percentualDesconto) {
}
