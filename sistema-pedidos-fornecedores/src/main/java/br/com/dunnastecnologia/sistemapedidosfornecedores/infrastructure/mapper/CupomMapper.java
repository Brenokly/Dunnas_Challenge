package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.mapper;

import org.springframework.stereotype.Component;

import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Cupom;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.cupom.CupomResponseDTO;

@Component
public class CupomMapper {
  public CupomResponseDTO toResponseDTO(Cupom cupom) {
    return new CupomResponseDTO(
        cupom.getId(), cupom.getCodigo(), cupom.getTipoDesconto(), cupom.getValor(),
        cupom.getDataValidade(), cupom.getValorMinimoPedido(), cupom.getLimiteDeUsos(),
        cupom.getUsosAtuais(), cupom.getAtivo(), cupom.getFornecedor().getId());
  }
}
