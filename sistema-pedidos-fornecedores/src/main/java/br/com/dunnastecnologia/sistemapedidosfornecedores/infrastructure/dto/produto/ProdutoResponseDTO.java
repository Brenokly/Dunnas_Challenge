package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.produto;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.categoria.CategoriaResponseDTO;

public record ProdutoResponseDTO(
                UUID id,
                String nome,
                String descricao,
                BigDecimal preco,
                BigDecimal percentualDesconto,
                UUID fornecedorId,
                Boolean ativo,
                Set<CategoriaResponseDTO> categorias) {
}