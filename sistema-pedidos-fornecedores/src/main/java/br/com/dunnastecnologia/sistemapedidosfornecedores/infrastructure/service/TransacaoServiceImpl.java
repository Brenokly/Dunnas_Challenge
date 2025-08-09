package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases.TransacaoUseCases;
import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Cliente;
import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.utils.enums.TipoTransacao;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.transacao.TransacaoResponseDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.mapper.TransacaoMapper;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.repository.TransacaoRepository;

@Service
public class TransacaoServiceImpl implements TransacaoUseCases {

  private final TransacaoRepository transacaoRepository;
  private final TransacaoMapper transacaoMapper;

  public TransacaoServiceImpl(TransacaoRepository transacaoRepository, TransacaoMapper transacaoMapper) {
    this.transacaoRepository = transacaoRepository;
    this.transacaoMapper = transacaoMapper;
  }

  @Override
  @Transactional(readOnly = true)
  public Page<TransacaoResponseDTO> listarTransacoesDoCliente(UserDetails authUser, Pageable pageable) {
    Cliente clienteLogado = getClienteFromUserDetails(authUser);
    return transacaoRepository.findAllByClienteId(clienteLogado.getId(), pageable)
        .map(transacaoMapper::toResponseDTO);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<TransacaoResponseDTO> listarTransacoesDoClientePorTipo(UserDetails authUser, TipoTransacao tipoTransacao,
      Pageable pageable) {
    Cliente clienteLogado = getClienteFromUserDetails(authUser);
    return transacaoRepository.findAllByClienteIdAndTipoTransacao(clienteLogado.getId(), tipoTransacao, pageable)
        .map(transacaoMapper::toResponseDTO);
  }

  private Cliente getClienteFromUserDetails(UserDetails userDetails) {
    if (!(userDetails instanceof Cliente)) {
      throw new AccessDeniedException("Ação permitida apenas para clientes autenticados.");
    }
    return (Cliente) userDetails;
  }
}