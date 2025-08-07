package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.service;

import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases.ClienteUseCases;
import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Cliente;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.ClienteRequestDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.ClienteResponseDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.ValorRequestDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.exception.RegraDeNegocioException;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.mapper.ClienteMapper;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ClienteServiceImpl implements ClienteUseCases {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClienteMapper clienteMapper;

    public ClienteServiceImpl(ClienteRepository clienteRepository, PasswordEncoder passwordEncoder, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
        this.clienteMapper = clienteMapper;
    }

    @Override
    @Transactional
    public ClienteResponseDTO cadastrarNovoCliente(ClienteRequestDTO requestDTO) {
        try {
            String senhaCriptografada = passwordEncoder.encode(requestDTO.senha());
            UUID novoClienteId = clienteRepository.registrarClienteViaFuncao(
                    requestDTO.nome(), requestDTO.cpf(), requestDTO.dataNascimento(), requestDTO.usuario(), senhaCriptografada
            );
            Cliente clienteSalvo = clienteRepository.findById(novoClienteId)
                    .orElseThrow(() -> new IllegalStateException("ERRO CRÍTICO: Cliente não encontrado após o cadastro via função."));
            return clienteMapper.toResponseDTO(clienteSalvo);
        } catch (DataIntegrityViolationException e) {
            throw new RegraDeNegocioException(e.getMostSpecificCause().getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarPorId(UUID id) {
        return clienteRepository.findById(id)
                .map(clienteMapper::toResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("Cliente com ID " + id + " não encontrado."));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClienteResponseDTO> listarTodos(Pageable pageable) {
        return clienteRepository.findAll(pageable).map(clienteMapper::toResponseDTO);
    }

    @Override
    @Transactional
    public ClienteResponseDTO adicionarSaldo(UUID id, ValorRequestDTO valorDTO) {
        if (!clienteRepository.existsById(id)) {
            throw new EntityNotFoundException("Cliente com ID " + id + " não encontrado.");
        }
        try {
            clienteRepository.adicionarSaldoViaFuncao(id, valorDTO.valor());
            return this.buscarPorId(id);
        } catch (DataIntegrityViolationException e) {
            throw new RegraDeNegocioException(e.getMostSpecificCause().getMessage());
        }
    }

    @Override
    @Transactional
    public void deletarCliente(UUID id) {
        if (!clienteRepository.existsById(id)) {
            throw new EntityNotFoundException("Cliente com ID " + id + " não encontrado.");
        }
        clienteRepository.deleteById(id);
    }
}
