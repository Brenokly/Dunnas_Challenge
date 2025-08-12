package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Pedido;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.pedido.PedidoFornecedorDetalhadoResponseDTO;

@Component
public class PedidoFornecedorMapper {

  private final ItemPedidoMapper itemPedidoMapper;

  public PedidoFornecedorMapper(ItemPedidoMapper itemPedidoMapper) {
    this.itemPedidoMapper = itemPedidoMapper;
  }

  public PedidoFornecedorDetalhadoResponseDTO toDetailedResponseDTO(Pedido pedido) {
    if (pedido == null) {
      return null;
    }

    return new PedidoFornecedorDetalhadoResponseDTO(
        pedido.getId(),
        pedido.getDataPedido(),
        pedido.getCliente().getNome(),
        pedido.getStatus().toString(),
        pedido.getItens().stream()
            .map(itemPedidoMapper::toResponseDTO)
            .collect(Collectors.toList()));
  }
}