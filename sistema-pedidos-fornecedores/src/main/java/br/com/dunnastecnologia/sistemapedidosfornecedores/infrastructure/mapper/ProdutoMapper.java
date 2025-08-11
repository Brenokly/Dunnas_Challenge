package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Produto;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.produto.ProdutoResponseDTO;

@Component
public class ProdutoMapper {

  private final CategoriaMapper categoriaMapper;

  public ProdutoMapper(CategoriaMapper categoriaMapper) {
    this.categoriaMapper = categoriaMapper;
  }

  public ProdutoResponseDTO toResponseDTO(Produto produto) {
    return new ProdutoResponseDTO(
        produto.getId(),
        produto.getNome(),
        produto.getDescricao(),
        produto.getPreco(),
        produto.getPercentualDesconto(),
        produto.getFornecedor().getId(),
        produto.getAtivo(),
        produto.getCategorias().stream()
            .map(categoriaMapper::toResponseDTO)
            .collect(Collectors.toSet()));
  }
}