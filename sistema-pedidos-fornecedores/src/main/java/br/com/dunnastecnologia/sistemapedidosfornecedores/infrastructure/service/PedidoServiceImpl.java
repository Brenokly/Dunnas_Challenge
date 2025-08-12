package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases.PedidoUseCases;
import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Cliente;
import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Fornecedor;
import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.ItensPedido;
import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Pedido;
import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.utils.enums.StatusPedido;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.pedido.PedidoFornecedorDetalhadoResponseDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.pedido.PedidoRequestDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.pedido.PedidoResponseDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.exception.RegraDeNegocioException;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.mapper.PedidoFornecedorMapper;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.mapper.PedidoMapper;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.repository.ItensPedidoRepository;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class PedidoServiceImpl implements PedidoUseCases {

  private final PedidoRepository pedidoRepository;
  private final ItensPedidoRepository itensPedidoRepository;
  private final PedidoMapper pedidoMapper;
  private final PedidoFornecedorMapper pedidoFornecedorMapper;
  private final ObjectMapper objectMapper;

  public PedidoServiceImpl(PedidoRepository pedidoRepository, ItensPedidoRepository itensPedidoRepository,
      PedidoMapper pedidoMapper, PedidoFornecedorMapper pedidoFornecedorMapper, ObjectMapper objectMapper) {
    this.pedidoRepository = pedidoRepository;
    this.itensPedidoRepository = itensPedidoRepository;
    this.pedidoMapper = pedidoMapper;
    this.pedidoFornecedorMapper = pedidoFornecedorMapper;
    this.objectMapper = objectMapper;
  }

  @Override
  @Transactional
  public PedidoResponseDTO criarNovoPedido(PedidoRequestDTO requestDTO, UserDetails authUser) {
    Cliente clienteLogado = getClienteFromUserDetails(authUser);

    String itensJson;
    try {
      itensJson = objectMapper.writeValueAsString(requestDTO.itens());
    } catch (JsonProcessingException e) {
      throw new RegraDeNegocioException("Formato inválido para os itens do pedido.");
    }

    UUID novoPedidoId = pedidoRepository.registrarPedidoViaFuncao(
        clienteLogado.getId(),
        itensJson,
        requestDTO.codigoCupom());

    Pedido pedidoSalvo = pedidoRepository.findById(novoPedidoId)
        .orElseThrow(() -> new IllegalStateException("ERRO CRÍTICO: Pedido não encontrado após o cadastro."));

    return pedidoMapper.toResponseDTO(pedidoSalvo);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<PedidoFornecedorDetalhadoResponseDTO> listarPedidosDoFornecedor(UserDetails authUser, Pageable pageable) {
    Fornecedor fornecedorLogado = getFornecedorFromUserDetails(authUser);

    Page<ItensPedido> itensPedidosPage = itensPedidoRepository.findByProdutoFornecedorId(fornecedorLogado.getId(),
        pageable);

    Set<Pedido> pedidosUnicos = itensPedidosPage.stream()
        .map(ItensPedido::getPedido)
        .collect(Collectors.toSet());

    List<PedidoFornecedorDetalhadoResponseDTO> dtos = pedidosUnicos.stream()
        .map(pedidoFornecedorMapper::toDetailedResponseDTO)
        .collect(Collectors.toList());

    return new PageImpl<>(dtos, pageable, itensPedidosPage.getTotalElements());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<PedidoResponseDTO> listarPedidosDoCliente(UserDetails authUser, Pageable pageable) {
    Cliente clienteLogado = getClienteFromUserDetails(authUser);
    return pedidoRepository.findAllByClienteId(clienteLogado.getId(), pageable)
        .map(pedidoMapper::toResponseDTO);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<PedidoResponseDTO> listarPedidosDoClientePorStatus(UserDetails authUser, StatusPedido status,
      Pageable pageable) {
    Cliente clienteLogado = getClienteFromUserDetails(authUser);
    return pedidoRepository.findAllByClienteIdAndStatus(clienteLogado.getId(), status, pageable)
        .map(pedidoMapper::toResponseDTO);
  }

  @Override
  @Transactional(readOnly = true)
  public PedidoResponseDTO buscarPedidoPorId(UUID id, UserDetails authUser) {
    Cliente clienteLogado = getClienteFromUserDetails(authUser);
    Pedido pedido = pedidoRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Pedido com ID " + id + " não encontrado."));

    if (!pedido.getCliente().getId().equals(clienteLogado.getId())) {
      throw new AccessDeniedException("Acesso negado. O pedido não pertence a este cliente.");
    }

    return pedidoMapper.toResponseDTO(pedido);
  }

  private Cliente getClienteFromUserDetails(UserDetails userDetails) {
    if (!(userDetails instanceof Cliente)) {
      throw new AccessDeniedException("Ação permitida apenas para clientes autenticados.");
    }
    return (Cliente) userDetails;
  }

  private Fornecedor getFornecedorFromUserDetails(UserDetails userDetails) {
    if (!(userDetails instanceof Fornecedor)) {
      throw new AccessDeniedException("Ação permitida apenas para fornecedores autenticados.");
    }
    return (Fornecedor) userDetails;
  }

}