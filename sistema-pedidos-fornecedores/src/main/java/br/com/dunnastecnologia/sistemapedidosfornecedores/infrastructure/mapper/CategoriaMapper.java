package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.mapper;

import org.springframework.stereotype.Component;

import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Categoria;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.categoria.CategoriaResponseDTO;

@Component
public class CategoriaMapper {

  public CategoriaResponseDTO toResponseDTO(Categoria categoria) {
    if (categoria == null) {
      return null;
    }

    return new CategoriaResponseDTO(
        categoria.getId(),
        categoria.getNome(),
        categoria.getAtivo());
  }
}
