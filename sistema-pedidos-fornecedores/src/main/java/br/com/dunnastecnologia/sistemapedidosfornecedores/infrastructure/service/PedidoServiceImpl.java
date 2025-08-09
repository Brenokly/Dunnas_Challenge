package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases.PedidoUseCases;
import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Cliente;
import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Pedido;
import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.utils.enums.StatusPedido;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.pedido.PedidoRequestDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.pedido.PedidoResponseDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.exception.RegraDeNegocioException;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.mapper.PedidoMapper;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class PedidoServiceImpl implements PedidoUseCases {

  private final PedidoRepository pedidoRepository;
  private final PedidoMapper pedidoMapper;
  private final ObjectMapper objectMapper;

  public PedidoServiceImpl(PedidoRepository pedidoRepository, PedidoMapper pedidoMapper, ObjectMapper objectMapper) {
    this.pedidoRepository = pedidoRepository;
    this.pedidoMapper = pedidoMapper;
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
}