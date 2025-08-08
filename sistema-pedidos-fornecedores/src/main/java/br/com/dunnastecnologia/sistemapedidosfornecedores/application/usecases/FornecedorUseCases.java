package br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.FornecedorRequestDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.FornecedorResponseDTO;

public interface FornecedorUseCases {

    FornecedorResponseDTO cadastrarNovoFornecedor(FornecedorRequestDTO requestDTO);

    Page<FornecedorResponseDTO> listarTodos(Pageable pageable);

    FornecedorResponseDTO buscarPorId(UUID id);

    void desativarFornecedor(UUID id);

    FornecedorResponseDTO reativarFornecedor(UUID id);
}
