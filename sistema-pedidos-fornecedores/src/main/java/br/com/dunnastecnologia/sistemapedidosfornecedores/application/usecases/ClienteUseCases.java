package br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.ClienteRequestDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.ClienteResponseDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.ValorRequestDTO;

public interface ClienteUseCases {

    ClienteResponseDTO cadastrarNovoCliente(ClienteRequestDTO requestDTO);

    ClienteResponseDTO buscarPorId(UUID id);

    Page<ClienteResponseDTO> listarTodos(Pageable pageable);

    ClienteResponseDTO adicionarSaldo(UUID id, ValorRequestDTO valorDTO);

    void desativarCliente(UUID id);
}
