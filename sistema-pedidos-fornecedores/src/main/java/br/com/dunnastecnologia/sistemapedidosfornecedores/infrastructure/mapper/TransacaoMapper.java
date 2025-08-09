package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.mapper;

import org.springframework.stereotype.Component;

import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Transacao;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.transacao.TransacaoResponseDTO;

@Component
public class TransacaoMapper {
  public TransacaoResponseDTO toResponseDTO(Transacao transacao) {
    return new TransacaoResponseDTO(
        transacao.getId(),
        transacao.getDataTransacao(),
        transacao.getTipoTransacao(),
        transacao.getValor(),
        transacao.getCliente().getId(),
        transacao.getPedido() != null ? transacao.getPedido().getId() : null);
  }
}