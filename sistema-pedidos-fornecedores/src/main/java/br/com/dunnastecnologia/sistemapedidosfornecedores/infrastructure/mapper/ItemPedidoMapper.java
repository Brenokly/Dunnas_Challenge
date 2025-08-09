package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.mapper;

import org.springframework.stereotype.Component;

import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.ItensPedido;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.pedido.ItemPedidoResponseDTO;

@Component
public class ItemPedidoMapper {

  public ItemPedidoResponseDTO toResponseDTO(ItensPedido itemPedido) {
    return new ItemPedidoResponseDTO(
        itemPedido.getProduto().getId(),
        itemPedido.getProduto().getNome(),
        itemPedido.getQuantidade(),
        itemPedido.getPrecoUnitarioCobrado());
  }
}