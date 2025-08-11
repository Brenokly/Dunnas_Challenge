package br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.produto.ProdutoRequestDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.produto.ProdutoResponseDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.exception.RegraDeNegocioException;
import jakarta.persistence.EntityNotFoundException;

public interface ProdutoUseCases {

  /**
   * Cadastra um novo produto para o fornecedor autenticado.
   *
   * @param requestDTO            Os dados do produto a ser cadastrado.
   * @param fornecedorAutenticado O principal de segurança do fornecedor logado.
   * @return Os dados do produto recém-criado.
   * @pre O usuário autenticado deve ser um Fornecedor.
   * @post O produto é salvo no repositório associado ao fornecedor.
   * @throws AccessDeniedException se o usuário autenticado não for um Fornecedor.
   */
  ProdutoResponseDTO cadastrarNovoProduto(ProdutoRequestDTO requestDTO, UserDetails fornecedorAutenticado);

  /**
   * Atualiza um produto existente, validando se pertence ao fornecedor
   * autenticado.
   *
   * @param produtoId             O ID do produto a ser atualizado.
   * @param requestDTO            Os novos dados do produto.
   * @param fornecedorAutenticado O principal de segurança do fornecedor logado.
   * @return Os dados do produto atualizado.
   * @pre O produto deve existir e pertencer ao fornecedor autenticado.
   * @post Os dados do produto são atualizados no repositório.
   * @throws EntityNotFoundException se o produto não for encontrado.
   * @throws AccessDeniedException   se o fornecedor não for o proprietário do
   *                                 produto.
   */
  ProdutoResponseDTO atualizarProduto(UUID produtoId, ProdutoRequestDTO requestDTO, UserDetails fornecedorAutenticado);

  /**
   * Desativa um produto (soft delete), validando se pertence ao fornecedor
   * autenticado.
   *
   * @param produtoId             O ID do produto a ser desativado.
   * @param fornecedorAutenticado O principal de segurança do fornecedor logado.
   * @pre O produto deve existir e pertencer ao fornecedor autenticado.
   * @post O produto é marcado como inativo no repositório.
   * @throws EntityNotFoundException se o produto não for encontrado.
   * @throws AccessDeniedException   se o fornecedor não for o proprietário do
   *                                 produto.
   */
  void desativarProduto(UUID produtoId, UserDetails fornecedorAutenticado);

  /**
   * Reativa um produto, validando se pertence ao fornecedor autenticado.
   *
   * @param produtoId             O ID do produto a ser reativado.
   * @param fornecedorAutenticado O principal de segurança do fornecedor logado.
   * @return Os dados do produto reativado.
   * @pre O produto deve existir e pertencer ao fornecedor autenticado.
   * @post O produto é marcado como ativo no repositório.
   * @throws EntityNotFoundException se o produto não for encontrado.
   * @throws AccessDeniedException   se o fornecedor não for o proprietário do
   *                                 produto.
   * @throws RegraDeNegocioException se o produto já estiver ativo.
   */
  ProdutoResponseDTO reativarProduto(UUID produtoId, UserDetails fornecedorAutenticado);

  /**
   * Busca um único produto ativo pelo seu ID (visão pública).
   *
   * @param id O ID do produto a ser buscado.
   * @return Os dados do produto encontrado.
   * @pre O produto deve existir e estar ativo.
   * @post Os dados do produto são retornados.
   * @throws EntityNotFoundException se o produto não for encontrado ou estiver
   *                                 inativo.
   */
  ProdutoResponseDTO buscarProdutoPublicoPorId(UUID id);

  /**
   * Lista os produtos ativos filtrados por um conjunto de categorias (visão
   * pública).
   *
   * @param categoriaIds O conjunto de IDs de categoria para filtrar os produtos.
   * @param pageable     As informações de paginação.
   * @return Uma lista paginada de produtos ativos.
   */
  Page<ProdutoResponseDTO> listarProdutosPublicos(Set<UUID> categoriaIds, Pageable pageable);

  /**
   * Lista todos os produtos (ativos e inativos) do fornecedor logado.
   *
   * @param fornecedorAutenticado O principal de segurança do fornecedor logado.
   * @param pageable              As informações de paginação.
   * @return Uma lista paginada dos produtos do fornecedor.
   * @pre O usuário autenticado deve ser um Fornecedor.
   * @post A lista de produtos do fornecedor é retornada, podendo estar vazia.
   * @throws AccessDeniedException se o usuário autenticado não for um Fornecedor.
   */
  Page<ProdutoResponseDTO> listarProdutosDoFornecedorLogado(UserDetails fornecedorAutenticado, Pageable pageable);

  /**
   * Lista os produtos ativos de um fornecedor específico (vitrine pública do
   * fornecedor).
   *
   * @param fornecedorId O ID do fornecedor.
   * @param pageable     As informações de paginação.
   * @return Uma lista paginada de produtos ativos do fornecedor.
   * @pre O fornecedor deve existir.
   * @post A lista de produtos é retornada, podendo estar vazia.
   */
  Page<ProdutoResponseDTO> listarProdutosAtivosDeFornecedor(UUID fornecedorId, Pageable pageable);
}