package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases.CategoriaUseCases;
import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Categoria;
import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Fornecedor;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.categoria.CategoriaRequestDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.categoria.CategoriaResponseDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.mapper.CategoriaMapper;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.repository.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoriaServiceImpl implements CategoriaUseCases {

  private final CategoriaRepository categoriaRepository;
  private final CategoriaMapper categoriaMapper;

  public CategoriaServiceImpl(CategoriaRepository categoriaRepository, CategoriaMapper categoriaMapper) {
    this.categoriaRepository = categoriaRepository;
    this.categoriaMapper = categoriaMapper;
  }

  @Override
  @Transactional
  public CategoriaResponseDTO criarNovaCategoria(CategoriaRequestDTO requestDTO, UserDetails authUser) {
    getFornecedorFromUserDetails(authUser);

    UUID novaCategoriaId = categoriaRepository.registrarCategoriaViaFuncao(requestDTO.nome());
    Categoria categoriaSalva = categoriaRepository.findById(novaCategoriaId)
        .orElseThrow(() -> new IllegalStateException("ERRO CRÍTICO: Categoria não encontrada após o cadastro."));
    return categoriaMapper.toResponseDTO(categoriaSalva);
  }

  @Override
  @Transactional
  public CategoriaResponseDTO atualizarCategoria(UUID id, CategoriaRequestDTO requestDTO, UserDetails authUser) {
    getFornecedorFromUserDetails(authUser);
    if (!categoriaRepository.existsById(id)) {
      throw new EntityNotFoundException("Categoria com ID " + id + " não encontrada.");
    }
    categoriaRepository.atualizarCategoriaViaProcedure(id, requestDTO.nome());
    return this.buscarCategoriaAtivaPorId(id);
  }

  @Override
  @Transactional
  public void desativarCategoria(UUID id, UserDetails authUser) {
    getFornecedorFromUserDetails(authUser);
    if (!categoriaRepository.existsById(id)) {
      throw new EntityNotFoundException("Categoria com ID " + id + " não encontrada.");
    }
    categoriaRepository.desativarCategoriaViaProcedure(id);
  }

  @Override
  @Transactional
  public CategoriaResponseDTO reativarCategoria(UUID id, UserDetails authUser) {
    getFornecedorFromUserDetails(authUser);
    if (!categoriaRepository.existsById(id)) {
      throw new EntityNotFoundException("Categoria com ID " + id + " não encontrada.");
    }
    categoriaRepository.reativarCategoriaViaProcedure(id);
    return this.buscarCategoriaAtivaPorId(id);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CategoriaResponseDTO> listarCategoriasAtivas(Pageable pageable) {
    return categoriaRepository.findAllByAtivoTrue(pageable).map(categoriaMapper::toResponseDTO);
  }

  @Override
  @Transactional(readOnly = true)
  public CategoriaResponseDTO buscarCategoriaAtivaPorId(UUID id) {
    return categoriaRepository.findByIdAndAtivoTrue(id)
        .map(categoriaMapper::toResponseDTO)
        .orElseThrow(() -> new EntityNotFoundException("Categoria com ID " + id + " não encontrada."));
  }

  private Fornecedor getFornecedorFromUserDetails(UserDetails userDetails) {
    if (!(userDetails instanceof Fornecedor)) {
      throw new AccessDeniedException("Ação permitida apenas para fornecedores autenticados.");
    }
    return (Fornecedor) userDetails;
  }
}