package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, UUID> {

        /**
         * Busca uma página de todos os produtos que estão ATIVOS no sistema.
         *
         * @param pageable O objeto contendo as informações de paginação.
         * @return uma Página (Page) de todos os Produtos ativos.
         * @pre O objeto pageable não deve ser nulo.
         * @post Uma página de produtos é retornada, podendo estar vazia.
         */
        Page<Produto> findAllByAtivoTrue(Pageable pageable);

        /**
         * Busca um produto específico pelo ID, mas somente se ele estiver ATIVO.
         *
         * @param id O ID do produto a ser buscado.
         * @return um Optional contendo o Produto, se encontrado e ativo.
         * @pre O ID não deve ser nulo.
         * @post O produto é retornado se encontrado e ativo, caso contrário, um
         *       Optional vazio é retornado.
         */
        Optional<Produto> findByIdAndAtivoTrue(UUID id);

        /**
         * Busca produtos ATIVOS cujo nome contém o termo de busca, ignorando maiúsculas
         * e minúsculas.
         *
         * @param nome     O termo a ser buscado no nome dos produtos.
         * @param pageable O objeto contendo as informações de paginação.
         * @return uma Página (Page) de Produtos que correspondem ao critério de busca.
         * @pre O termo de busca não deve ser nulo.
         * @post Uma página de produtos correspondentes é retornada, podendo estar
         *       vazia.
         */
        Page<Produto> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome, Pageable pageable);

        /**
         * Busca uma página de produtos ATIVOS de um fornecedor específico.
         *
         * @param fornecedorId O ID do fornecedor.
         * @param pageable     O objeto contendo as informações de paginação.
         * @return uma Página (Page) de Produtos ativos do fornecedor.
         * @pre O ID do fornecedor deve ser válido.
         * @post Uma página de produtos é retornada, podendo estar vazia.
         */
        Page<Produto> findAllByAtivoTrueAndFornecedorId(UUID fornecedorId, Pageable pageable);

        /**
         * Busca uma página de todos os produtos (ativos e inativos) de um fornecedor
         * específico.
         *
         * @param fornecedorId O ID do fornecedor.
         * @param pageable     O objeto contendo as informações de paginação.
         * @return uma Página (Page) de Produtos do fornecedor.
         * @pre O ID do fornecedor deve ser válido.
         * @post Uma página de produtos é retornada, podendo estar vazia.
         */
        Page<Produto> findAllByFornecedorId(UUID fornecedorId, Pageable pageable);

        /**
         * Chama a função 'cadastrar_novo_produto' no PostgreSQL.
         *
         * @return o UUID do produto recém-criado.
         */
        @Query(value = "SELECT cadastrar_novo_produto(:nome, :descricao, :preco, :percentualDesconto, :fornecedorId)", nativeQuery = true)
        UUID registrarProdutoViaFuncao(
                        @Param("nome") String nome,
                        @Param("descricao") String descricao,
                        @Param("preco") BigDecimal preco,
                        @Param("percentualDesconto") BigDecimal percentualDesconto,
                        @Param("fornecedorId") UUID fornecedorId);

        /**
         * Chama o procedimento 'atualizar_produto' no PostgreSQL, que valida a
         * propriedade antes de alterar.
         */
        @Modifying
        @Query(value = "CALL atualizar_produto(:produtoId, :fornecedorId, :nome, :descricao, :preco, :percentualDesconto)", nativeQuery = true)
        void atualizarProdutoViaProcedure(
                        @Param("produtoId") UUID produtoId,
                        @Param("fornecedorId") UUID fornecedorId,
                        @Param("nome") String nome,
                        @Param("descricao") String descricao,
                        @Param("preco") BigDecimal preco,
                        @Param("percentualDesconto") BigDecimal percentualDesconto);

        /**
         * Chama o procedimento 'desativar_produto' no PostgreSQL, que valida a
         * propriedade.
         */
        @Modifying
        @Query(value = "CALL desativar_produto(:produtoId, :fornecedorId)", nativeQuery = true)
        void desativarProdutoViaProcedure(
                        @Param("produtoId") UUID produtoId,
                        @Param("fornecedorId") UUID fornecedorId);

        /**
         * Chama o procedimento 'reativar_produto' no PostgreSQL, que valida a
         * propriedade.
         */
        @Modifying
        @Query(value = "CALL reativar_produto(:produtoId, :fornecedorId)", nativeQuery = true)
        void reativarProdutoViaProcedure(
                        @Param("produtoId") UUID produtoId,
                        @Param("fornecedorId") UUID fornecedorId);
}