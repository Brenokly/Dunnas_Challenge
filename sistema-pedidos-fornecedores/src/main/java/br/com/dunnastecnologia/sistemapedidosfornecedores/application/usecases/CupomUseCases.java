package br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.cupom.CupomRequestDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.cupom.CupomResponseDTO;

public interface CupomUseCases {
  CupomResponseDTO criarNovoCupom(CupomRequestDTO requestDTO);

  Page<CupomResponseDTO> listarTodos(Pageable pageable);

  CupomResponseDTO buscarPorId(UUID id);
}
