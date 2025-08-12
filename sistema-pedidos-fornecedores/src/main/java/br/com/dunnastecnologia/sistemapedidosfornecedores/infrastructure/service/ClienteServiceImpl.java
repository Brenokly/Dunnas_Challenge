package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.service;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases.ClienteUseCases;
import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Cliente;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.cliente.ClienteRequestDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.cliente.ClienteResponseDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.cliente.ClienteUpdateDadosPessoaisDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.cliente.ClienteUpdateSenhaDTO;
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
    public ClienteResponseDTO buscarClienteLogado(UserDetails authUser) {
        Cliente clienteLogado = getClienteFromUserDetails(authUser);
        return clienteRepository.findById(clienteLogado.getId())
                .map(clienteMapper::toResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado."));
    }

    @Override
    @Transactional
    public ClienteResponseDTO atualizarDadosPessoais(UUID id, ClienteUpdateDadosPessoaisDTO requestDTO,
            UserDetails authUser) {
        Cliente clienteLogado = getClienteFromUserDetails(authUser);
        if (!clienteLogado.getId().equals(id)) {
            throw new AccessDeniedException("Um cliente só pode atualizar a própria conta.");
        }

        Cliente clienteAtual = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente com ID " + id + " não encontrado."));

        String novoNome = requestDTO.nome() != null ? requestDTO.nome() : clienteAtual.getNome();
        LocalDate novaDataNascimento = requestDTO.dataNascimento() != null ? requestDTO.dataNascimento()
                : clienteAtual.getDataNascimento();

        clienteRepository.atualizarClienteViaProcedure(id, novoNome, novaDataNascimento, null, null);

        return this.buscarClienteLogado(authUser);
    }

    @Override
    @Transactional
    public void atualizarSenha(UUID id, ClienteUpdateSenhaDTO requestDTO, UserDetails authUser) {
        Cliente clienteLogado = getClienteFromUserDetails(authUser);
        if (!clienteLogado.getId().equals(id)) {
            throw new AccessDeniedException("Um cliente só pode alterar a própria senha.");
        }
        if (!requestDTO.novaSenha().equals(requestDTO.confirmacaoNovaSenha())) {
            throw new RegraDeNegocioException("A nova senha e a confirmação não correspondem.");
        }

        String novaSenhaHash = passwordEncoder.encode(requestDTO.novaSenha());

        clienteRepository.atualizarClienteViaProcedure(id, null, null, requestDTO.senhaAtual(), novaSenhaHash);
    }

    @Override
    @Transactional
    public ClienteResponseDTO adicionarSaldo(UserDetails authUser, ValorRequestDTO valorDTO) {
        Cliente clienteLogado = getClienteFromUserDetails(authUser);
        clienteRepository.adicionarSaldoViaFuncao(clienteLogado.getId(), valorDTO.valor());
        return this.buscarClienteLogado(authUser);
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

    private Cliente getClienteFromUserDetails(UserDetails userDetails) {
        if (!(userDetails instanceof Cliente)) {
            throw new AccessDeniedException("Ação permitida apenas para clientes autenticados.");
        }
        return (Cliente) userDetails;
    }
}
