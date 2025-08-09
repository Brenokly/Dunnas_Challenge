package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.produto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProdutoResponseDTO(
    UUID id,
    String nome,
    String descricao,
    BigDecimal preco,
    BigDecimal percentualDesconto,
    UUID fornecedorId,
    Boolean ativo) {
}