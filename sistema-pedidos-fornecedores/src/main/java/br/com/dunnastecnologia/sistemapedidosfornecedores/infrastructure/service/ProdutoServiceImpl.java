package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.service;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases.ProdutoUseCases;
import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Fornecedor;
import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Produto;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.produto.ProdutoRequestDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.produto.ProdutoResponseDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.exception.RegraDeNegocioException;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.mapper.ProdutoMapper;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ProdutoServiceImpl implements ProdutoUseCases {

  private final ProdutoRepository produtoRepository;
  private final ProdutoMapper produtoMapper;

  public ProdutoServiceImpl(ProdutoRepository produtoRepository, ProdutoMapper produtoMapper) {
    this.produtoRepository = produtoRepository;
    this.produtoMapper = produtoMapper;
  }

  @Override
  @Transactional
  public ProdutoResponseDTO cadastrarNovoProduto(ProdutoRequestDTO requestDTO, UserDetails fornecedorAutenticado) {
    Fornecedor fornecedor = getFornecedorFromUserDetails(fornecedorAutenticado);

    UUID[] categoriaIdsArray = requestDTO.categoriaIds().toArray(new UUID[0]);

    UUID novoProdutoId = produtoRepository.registrarProdutoViaFuncao(
        requestDTO.nome(),
        requestDTO.descricao(),
        requestDTO.preco(),
        requestDTO.percentualDesconto(),
        fornecedor.getId(),
        categoriaIdsArray);

    Produto produtoSalvo = produtoRepository.findById(novoProdutoId)
        .orElseThrow(() -> new IllegalStateException("ERRO CRÍTICO: Produto não encontrado após o cadastro."));
    return produtoMapper.toResponseDTO(produtoSalvo);
  }

  @Override
  @Transactional
  public ProdutoResponseDTO atualizarProduto(UUID produtoId, ProdutoRequestDTO requestDTO,
      UserDetails fornecedorAutenticado) {
    Fornecedor fornecedor = getFornecedorFromUserDetails(fornecedorAutenticado);

    UUID[] categoriaIdsArray = requestDTO.categoriaIds().toArray(new UUID[0]);

    produtoRepository.atualizarProdutoViaProcedure(
        produtoId,
        fornecedor.getId(),
        requestDTO.nome(),
        requestDTO.descricao(),
        requestDTO.preco(),
        requestDTO.percentualDesconto(),
        categoriaIdsArray);

    return this.buscarProdutoPublicoPorId(produtoId);
  }

  @Override
  @Transactional
  public void desativarProduto(UUID produtoId, UserDetails fornecedorAutenticado) {
    Fornecedor fornecedor = getFornecedorFromUserDetails(fornecedorAutenticado);
    produtoRepository.desativarProdutoViaProcedure(produtoId, fornecedor.getId());
  }

  @Override
  @Transactional
  public ProdutoResponseDTO reativarProduto(UUID produtoId, UserDetails fornecedorAutenticado) {
    Fornecedor fornecedor = getFornecedorFromUserDetails(fornecedorAutenticado);

    Produto produto = produtoRepository.findById(produtoId)
        .orElseThrow(() -> new EntityNotFoundException("Produto com ID " + produtoId + " não encontrado."));
    if (produto.getAtivo()) {
      throw new RegraDeNegocioException("Este produto já está ativo.");
    }

    produtoRepository.reativarProdutoViaProcedure(produtoId, fornecedor.getId());
    return produtoRepository.findById(produtoId)
        .map(produtoMapper::toResponseDTO)
        .orElseThrow(() -> new EntityNotFoundException("Produto com ID " + produtoId + " não encontrado."));
  }

  @Override
  @Transactional(readOnly = true)
  public ProdutoResponseDTO buscarProdutoPublicoPorId(UUID id) {
    return produtoRepository.findByIdPublicoAtivo(id)
        .map(produtoMapper::toResponseDTO)
        .orElseThrow(() -> new EntityNotFoundException("Produto com ID " + id + " não encontrado."));
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ProdutoResponseDTO> listarProdutosPublicos(Set<UUID> categoriaIds, Pageable pageable) {
    if (categoriaIds == null || categoriaIds.isEmpty()) {
      return produtoRepository.findAllPublicosAtivos(pageable).map(produtoMapper::toResponseDTO);
    } else {
      return produtoRepository.findAllByCategoriasInPublicoAtivo(categoriaIds, pageable)
          .map(produtoMapper::toResponseDTO);
    }
  }

  @Transactional(readOnly = true)
  public Page<ProdutoResponseDTO> listarProdutosDoFornecedorLogado(UserDetails authUser, Pageable pageable) {
    Fornecedor fornecedorLogado = getFornecedorFromUserDetails(authUser);

    Sort sort = Sort.by(Sort.Direction.DESC, "ativo")
        .and(Sort.by(Sort.Direction.DESC, "dataCriacao"));

    Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

    Page<Produto> produtos = produtoRepository.findAllByFornecedorId(fornecedorLogado.getId(), sortedPageable);

    return produtos.map(produtoMapper::toResponseDTO);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ProdutoResponseDTO> listarProdutosAtivosDeFornecedor(UUID fornecedorId, Pageable pageable) {
    return produtoRepository.findAllByFornecedorIdPublicoAtivo(fornecedorId, pageable)
        .map(produtoMapper::toResponseDTO);
  }

  private Fornecedor getFornecedorFromUserDetails(UserDetails userDetails) {
    if (!(userDetails instanceof Fornecedor)) {
      throw new AccessDeniedException("Ação permitida apenas para fornecedores autenticados.");
    }
    return (Fornecedor) userDetails;
  }
}