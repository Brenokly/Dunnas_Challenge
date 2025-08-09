package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases.ClienteUseCases;
import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Cliente;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.cliente.ClienteRequestDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.cliente.ClienteResponseDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.cliente.ValorRequestDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.exception.RegraDeNegocioException;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.mapper.ClienteMapper;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ClienteServiceImpl implements ClienteUseCases {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClienteMapper clienteMapper;

    public ClienteServiceImpl(ClienteRepository clienteRepository, PasswordEncoder passwordEncoder,
            ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
        this.clienteMapper = clienteMapper;
    }

    @Override
    @Transactional
    public ClienteResponseDTO cadastrarNovoCliente(ClienteRequestDTO requestDTO) {
        String senhaCriptografada = passwordEncoder.encode(requestDTO.senha());
        UUID novoClienteId = clienteRepository.registrarClienteViaFuncao(
                requestDTO.nome(), requestDTO.cpf(), requestDTO.dataNascimento(), requestDTO.usuario(),
                senhaCriptografada);

        Cliente clienteSalvo = clienteRepository.findById(novoClienteId)
                .orElseThrow(() -> new IllegalStateException(
                        "ERRO CRÍTICO: Cliente não encontrado após o cadastro via função."));

        return clienteMapper.toResponseDTO(clienteSalvo);
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarPorId(UUID id) {
        return clienteRepository.findByIdAndAtivoTrue(id)
                .map(clienteMapper::toResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("Cliente com ID " + id + " não encontrado."));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClienteResponseDTO> listarTodos(Pageable pageable) {
        return clienteRepository.findAllByAtivoTrue(pageable).map(clienteMapper::toResponseDTO);
    }

    @Override
    @Transactional
    public ClienteResponseDTO adicionarSaldo(UUID id, ValorRequestDTO valorDTO) {
        if (!clienteRepository.existsById(id)) {
            throw new EntityNotFoundException("Cliente com ID " + id + " não encontrado.");
        }
        clienteRepository.adicionarSaldoViaFuncao(id, valorDTO.valor());
        return this.buscarPorId(id);
    }

    @Override
    @Transactional
    public void desativarCliente(UUID id, UserDetails authUser) {
        Cliente clienteLogado = getClienteFromUserDetails(authUser);

        if (!clienteLogado.getId().equals(id)) {
            throw new AccessDeniedException("Um cliente só pode desativar a própria conta.");
        }

        Cliente clienteParaDesativar = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente com ID " + id + " não encontrado."));
        if (!clienteParaDesativar.getAtivo()) {
            throw new RegraDeNegocioException("Este cliente já está inativo.");
        }
        clienteRepository.desativarClienteViaProcedure(id);
    }

    @Override
    @Transactional
    public ClienteResponseDTO reativarCliente(UUID id, UserDetails authUser) {
        Cliente clienteLogado = getClienteFromUserDetails(authUser);

        if (!clienteLogado.getId().equals(id)) {
            throw new AccessDeniedException("Um cliente só pode reativar a própria conta.");
        }

        Cliente clienteParaReativar = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente com ID " + id + " não encontrado."));
        if (clienteParaReativar.getAtivo()) {
            throw new RegraDeNegocioException("Este cliente já está ativo.");
        }

        clienteRepository.reativarClienteViaProcedure(id);
        return this.buscarPorId(id);
    }

    private Cliente getClienteFromUserDetails(UserDetails userDetails) {
        if (!(userDetails instanceof Cliente)) {
            throw new AccessDeniedException("Ação permitida apenas para clientes autenticados.");
        }
        return (Cliente) userDetails;
    }
}
