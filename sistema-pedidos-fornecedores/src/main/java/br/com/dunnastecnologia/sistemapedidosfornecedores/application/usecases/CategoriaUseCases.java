package br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.categoria.CategoriaRequestDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.categoria.CategoriaResponseDTO;
import jakarta.persistence.EntityNotFoundException;

public interface CategoriaUseCases {

  /**
   * Cria uma nova categoria. Ação restrita a usuários autenticados.
   *
   * @param requestDTO Os dados da categoria a ser criada.
   * @param authUser   O principal de segurança do usuário logado.
   * @return Os dados da categoria recém-criada.
   * @pre O usuário autenticado deve ter permissão para criar categorias (ex: ser
   *      um Fornecedor).
   * @post A categoria é salva no repositório.
   * @throws AccessDeniedException                                                                               se
   *                                                                                                             o
   *                                                                                                             usuário
   *                                                                                                             não
   *                                                                                                             tiver
   *                                                                                                             permissão.
   * @throws br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.exception.RegraDeNegocioException se
   *                                                                                                             o
   *                                                                                                             nome
   *                                                                                                             da
   *                                                                                                             categoria
   *                                                                                                             já
   *                                                                                                             existir.
   */
  CategoriaResponseDTO criarNovaCategoria(CategoriaRequestDTO requestDTO, UserDetails authUser);

  /**
   * Atualiza o nome de uma categoria existente.
   *
   * @param id         O ID da categoria a ser atualizada.
   * @param requestDTO Os novos dados da categoria.
   * @param authUser   O principal de segurança do usuário logado.
   * @return Os dados da categoria atualizada.
   * @pre A categoria deve existir e o usuário deve ter permissão.
   * @post O nome da categoria é atualizado.
   * @throws EntityNotFoundException se a categoria não for encontrada.
   * @throws AccessDeniedException   se o usuário não tiver permissão.
   */
  CategoriaResponseDTO atualizarCategoria(UUID id, CategoriaRequestDTO requestDTO, UserDetails authUser);

  /**
   * Desativa uma categoria (soft delete).
   *
   * @param id       O ID da categoria a ser desativada.
   * @param authUser O principal de segurança do usuário logado.
   * @pre A categoria deve existir e o usuário deve ter permissão.
   * @post A categoria é marcada como inativa.
   * @throws EntityNotFoundException se a categoria não for encontrada.
   * @throws AccessDeniedException   se o usuário não tiver permissão.
   */
  void desativarCategoria(UUID id, UserDetails authUser);

  /**
   * Reativa uma categoria.
   *
   * @param id       O ID da categoria a ser reativada.
   * @param authUser O principal de segurança do usuário logado.
   * @return Os dados da categoria reativada.
   * @pre A categoria deve existir e o usuário deve ter permissão.
   * @post A categoria é marcada como ativa.
   * @throws EntityNotFoundException se a categoria não for encontrada.
   * @throws AccessDeniedException   se o usuário não tiver permissão.
   */
  CategoriaResponseDTO reativarCategoria(UUID id, UserDetails authUser);

  /**
   * Lista todas as categorias ativas de forma paginada (visão pública).
   *
   * @param pageable As informações de paginação.
   * @return Uma lista paginada de categorias ativas.
   * @pre Nenhuma.
   * @post A lista de categorias é retornada, podendo estar vazia.
   */
  Page<CategoriaResponseDTO> listarCategoriasAtivas(Pageable pageable);

  /**
   * Busca uma categoria ativa pelo seu ID (visão pública).
   *
   * @param id O ID da categoria.
   * @return Os dados da categoria encontrada.
   * @pre A categoria deve existir e estar ativa.
   * @post Os dados da categoria são retornados.
   * @throws EntityNotFoundException se a categoria não for encontrada ou estiver
   *                                 inativa.
   */
  CategoriaResponseDTO buscarCategoriaAtivaPorId(UUID id);
}