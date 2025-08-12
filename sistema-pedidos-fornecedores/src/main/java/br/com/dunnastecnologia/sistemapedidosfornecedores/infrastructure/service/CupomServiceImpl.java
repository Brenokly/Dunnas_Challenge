package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases.CupomUseCases;
import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Cupom;
import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Fornecedor;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.cupom.CupomRequestDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.cupom.CupomResponseDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.mapper.CupomMapper;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.repository.CupomRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class CupomServiceImpl implements CupomUseCases {
  private final CupomRepository cupomRepository;
  private final CupomMapper cupomMapper;

  public CupomServiceImpl(CupomRepository cupomRepository, CupomMapper cupomMapper) {
    this.cupomRepository = cupomRepository;
    this.cupomMapper = cupomMapper;
  }

  @Override
  @Transactional
  public CupomResponseDTO criarNovoCupom(CupomRequestDTO requestDTO, UserDetails authUser) {
    Fornecedor fornecedor = getFornecedorFromUserDetails(authUser);
    UUID novoCupomId = cupomRepository.registrarCupomViaFuncao(
        requestDTO.codigo(), requestDTO.tipoDesconto(), requestDTO.valor(),
        requestDTO.dataValidade(), requestDTO.valorMinimoPedido(),
        requestDTO.limiteDeUsos(), fornecedor.getId());
    Cupom cupomSalvo = cupomRepository.findById(novoCupomId)
        .orElseThrow(() -> new IllegalStateException("ERRO CRÍTICO: Cupom não encontrado após cadastro."));
    return cupomMapper.toResponseDTO(cupomSalvo);
  }

  @Transactional(readOnly = true)
  public CupomResponseDTO buscarCupomPorFornecedorECodigo(UUID fornecedorId, String codigo) {
    Optional<Cupom> cupom = cupomRepository.findByFornecedorIdAndCodigoAtivos(fornecedorId, codigo);
    return cupom.map(cupomMapper::toResponseDTO)
        .orElseThrow(() -> new EntityNotFoundException("Cupom não encontrado."));
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CupomResponseDTO> listarCuponsDoFornecedor(UserDetails authUser, Pageable pageable) {
    Fornecedor fornecedor = getFornecedorFromUserDetails(authUser);
    return cupomRepository.findAllByFornecedorId(fornecedor.getId(), pageable)
        .map(cupomMapper::toResponseDTO);
  }

  @Override
  @Transactional(readOnly = true)
  public CupomResponseDTO buscarPorIdDoFornecedor(UUID id, UserDetails authUser) {
    Fornecedor fornecedor = getFornecedorFromUserDetails(authUser);
    Cupom cupom = cupomRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Cupom com ID " + id + " não encontrado."));
    if (!cupom.getFornecedor().getId().equals(fornecedor.getId())) {
      throw new AccessDeniedException("Acesso negado. O cupom não pertence a este fornecedor.");
    }
    return cupomMapper.toResponseDTO(cupom);
  }

  @Override
  public void desativarCupom(UUID id, UserDetails authUser) {
    Fornecedor fornecedor = getFornecedorFromUserDetails(authUser);

    Cupom cupom = cupomRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Cupom com ID " + id + " não encontrado."));

    if (!cupom.getFornecedor().getId().equals(fornecedor.getId())) {
      throw new AccessDeniedException("Acesso negado. O cupom não pertence a este fornecedor.");
    }

    cupomRepository.desativarCupomViaProcedure(id, fornecedor.getId());
  }

  @Override
  public CupomResponseDTO reativarCupom(UUID id, UserDetails authUser) {
    Fornecedor fornecedor = getFornecedorFromUserDetails(authUser);

    Cupom cupom = cupomRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Cupom com ID " + id + " não encontrado."));

    if (!cupom.getFornecedor().getId().equals(fornecedor.getId())) {
      throw new AccessDeniedException("Acesso negado. O cupom não pertence a este fornecedor.");
    }

    cupomRepository.reativarCupomViaProcedure(id, fornecedor.getId());
    return cupomMapper.toResponseDTO(cupom);
  }

  private Fornecedor getFornecedorFromUserDetails(UserDetails userDetails) {
    if (!(userDetails instanceof Fornecedor)) {
      throw new AccessDeniedException("Ação permitida apenas para usuários autenticados.");
    }
    return (Fornecedor) userDetails;
  }
}
