package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {

  /**
   * Busca uma página de todas as categorias que estão ATIVAS.
   *
   * @param pageable O objeto contendo as informações de paginação.
   * @return uma Página (Page) de todas as Categorias ativas.
   * @pre O objeto pageable não deve ser nulo.
   * @post Uma página de categorias é retornada, podendo estar vazia.
   */
  Page<Categoria> findAllByAtivoTrue(Pageable pageable);

  /**
   * Busca uma categoria específica pelo ID, mas somente se ela estiver ATIVA.
   *
   * @param id O ID da categoria a ser buscada.
   * @return um Optional contendo a Categoria, se encontrada e ativa.
   * @pre O ID não deve ser nulo.
   * @post A categoria é retornada se encontrada e ativa, caso contrário, um
   *       Optional vazio é retornado.
   */
  Optional<Categoria> findByIdAndAtivoTrue(UUID id);

  /**
   * Verifica se uma categoria existe e está ATIVA pelo seu ID.
   *
   * @param id O ID da categoria a ser verificada.
   * @return 'true' se a categoria existir e estiver ativa, caso contrário
   *         'false'.
   * @pre O ID não deve ser nulo.
   * @post O resultado booleano é retornado.
   */
  boolean existsByIdAndAtivoTrue(UUID id);

  /**
   * Chama a função 'cadastrar_nova_categoria' no PostgreSQL.
   *
   * @param nome O nome da nova categoria.
   * @return o UUID da categoria recém-criada.
   * @pre O nome não deve ser duplicado (case-insensitive).
   * @post Uma nova categoria é criada no banco de dados.
   * @throws RuntimeException se a regra de negócio de nome único for violada.
   */
  @Query(value = "SELECT cadastrar_nova_categoria(:nome)", nativeQuery = true)
  UUID registrarCategoriaViaFuncao(@Param("nome") String nome);

  /**
   * Chama o procedimento 'atualizar_categoria' no PostgreSQL.
   *
   * @param categoriaId O ID da categoria a ser atualizada.
   * @param novoNome    O novo nome para a categoria.
   * @pre A categoria deve existir.
   * @post O nome da categoria é atualizado.
   * @throws RuntimeException se a regra de negócio de nome único for violada.
   */
  @Modifying
  @Query(value = "CALL atualizar_categoria(:categoriaId, :novoNome)", nativeQuery = true)
  void atualizarCategoriaViaProcedure(@Param("categoriaId") UUID categoriaId, @Param("novoNome") String novoNome);

  /**
   * Chama o procedimento 'desativar_categoria' no PostgreSQL.
   *
   * @param categoriaId ID da categoria a ser desativada.
   * @pre A categoria deve existir.
   * @post A categoria é marcada como inativa.
   */
  @Modifying
  @Query(value = "CALL desativar_categoria(:categoriaId)", nativeQuery = true)
  void desativarCategoriaViaProcedure(@Param("categoriaId") UUID categoriaId);

  /**
   * Chama o procedimento 'reativar_categoria' no PostgreSQL.
   *
   * @param categoriaId ID da categoria a ser reativada.
   * @pre A categoria deve existir.
   * @post A categoria é marcada como ativa.
   */
  @Modifying
  @Query(value = "CALL reativar_categoria(:categoriaId)", nativeQuery = true)
  void reativarCategoriaViaProcedure(@Param("categoriaId") UUID categoriaId);
}