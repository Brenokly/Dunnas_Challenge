package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.mapper;

import org.springframework.stereotype.Component;

import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Produto;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.produto.ProdutoResponseDTO;

@Component
public class ProdutoMapper {
  public ProdutoResponseDTO toResponseDTO(Produto produto) {
    return new ProdutoResponseDTO(
        produto.getId(),
        produto.getNome(),
        produto.getDescricao(),
        produto.getPreco(),
        produto.getPercentualDesconto(),
        produto.getFornecedor().getId(),
        produto.getAtivo());
  }
}