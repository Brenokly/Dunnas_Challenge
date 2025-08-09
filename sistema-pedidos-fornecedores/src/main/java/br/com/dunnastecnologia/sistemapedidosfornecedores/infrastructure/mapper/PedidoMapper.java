package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Pedido;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.pedido.PedidoResponseDTO;

@Component
public class PedidoMapper {

  private final ItemPedidoMapper itemPedidoMapper;

  public PedidoMapper(ItemPedidoMapper itemPedidoMapper) {
    this.itemPedidoMapper = itemPedidoMapper;
  }

  public PedidoResponseDTO toResponseDTO(Pedido pedido) {
    return new PedidoResponseDTO(
        pedido.getId(),
        pedido.getDataPedido(),
        pedido.getValorBruto(),
        pedido.getValorDesconto(),
        pedido.getValorFinal(),
        pedido.getStatus(),
        pedido.getCliente().getId(),
        pedido.getCupom() != null ? pedido.getCupom().getCodigo() : null,
        pedido.getItens().stream()
            .map(itemPedidoMapper::toResponseDTO)
            .collect(Collectors.toList()));
  }
}