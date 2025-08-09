package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases.CupomUseCases;
import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Cupom;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.cupom.CupomRequestDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.cupom.CupomResponseDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.mapper.CupomMapper;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.repository.CupomRepository;

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
  public CupomResponseDTO criarNovoCupom(CupomRequestDTO requestDTO) {
    UUID novoCupomId = cupomRepository.registrarCupomViaFuncao(
        requestDTO.codigo(), requestDTO.tipoDesconto(),
        requestDTO.valor(), requestDTO.dataValidade());
    Cupom cupomSalvo = cupomRepository.findById(novoCupomId)
        .orElseThrow(() -> new IllegalStateException("ERRO CRÍTICO: Cupom não encontrado após cadastro."));
    return cupomMapper.toResponseDTO(cupomSalvo);
  }

  @Override
  public Page<CupomResponseDTO> listarTodos(Pageable pageable) {
    Page<Cupom> cupons = cupomRepository.findAll(pageable);
    return cupons.map(cupomMapper::toResponseDTO);
  }

  @Override
  public CupomResponseDTO buscarPorId(UUID id) {
    Cupom cupom = cupomRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Cupom não encontrado"));
    return cupomMapper.toResponseDTO(cupom);
  }
}
