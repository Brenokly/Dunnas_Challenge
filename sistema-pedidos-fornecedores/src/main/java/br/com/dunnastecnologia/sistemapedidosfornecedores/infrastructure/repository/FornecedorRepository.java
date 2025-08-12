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

import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Fornecedor;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, UUID> {

        /**
         * Busca todos os fornecedores ativos.
         *
         * @param pageable informações de paginação.
         * @return lista de fornecedores ativos paginada.
         * @pre Nenhuma.
         * @post A lista de fornecedores ativos é retornada.
         */
        Page<Fornecedor> findAllByAtivoTrue(Pageable pageable);

        /**
         * Busca um fornecedor ativo pelo seu ID.
         *
         * @param id ID do fornecedor a ser buscado.
         * @return um Optional contendo o Fornecedor, se encontrado.
         * @pre O fornecedor deve existir e estar ativo.
         * @post O fornecedor é retornado se encontrado, caso contrário, um Optional
         *       vazio é retornado.
         */
        Optional<Fornecedor> findByIdAndAtivoTrue(UUID id);

        /**
         * Busca um fornecedor ativo pelo nome de usuário.
         *
         * @param usuario nome de usuário do fornecedor a ser buscado.
         * @return um Optional contendo o Fornecedor, se encontrado.
         * @pre O fornecedor deve existir e estar ativo.
         * @post O fornecedor é retornado se encontrado, caso contrário, um Optional
         *       vazio é retornado.
         */
        Optional<Fornecedor> findByUsuarioAndAtivoTrue(String usuario);

        /**
         * Chama a função 'cadastrar_novo_fornecedor' no PostgreSQL para registrar
         * um novo fornecedor. Delega a lógica de validação de duplicidade e
         * inserção para o banco de dados.
         *
         * @return o UUID do fornecedor recém-criado.
         */
        @Query(value = "SELECT cadastrar_novo_fornecedor(:nome, :cnpj, :usuario, :senhaHash)", nativeQuery = true)
        UUID registrarFornecedorViaFuncao(
                        @Param("nome") String nome,
                        @Param("cnpj") String cnpj,
                        @Param("usuario") String usuario,
                        @Param("senhaHash") String senhaHash);

        /**
         * Chama o procedimento 'atualizar_fornecedor' no PostgreSQL, que lida com a
         * atualização
         * de dados pessoais e a validação/atualização opcional de senha.
         */
        @Modifying
        @Query(value = "CALL atualizar_senha_fornecedor(:fornecedorId, :senhaAtual, :novoSenhaHash)", nativeQuery = true)
        void atualizarSenhaFornecedorViaProcedure(
                        @Param("fornecedorId") UUID fornecedorId,
                        @Param("senhaAtual") String senhaAtual,
                        @Param("novoSenhaHash") String novoSenhaHash);

        /**
         * Chama a função 'desativar_fornecedor' no PostgreSQL. Delega a transação
         * atômica de desativar o fornecedor para o banco de dados.
         *
         * @param fornecedorId ID do fornecedor a ser desativado.
         * @pre O fornecedor deve existir e estar ativo.
         * @post O fornecedor é desativado.
         */
        @Modifying
        @Query(value = "CALL desativar_fornecedor(:fornecedorId)", nativeQuery = true)
        void desativarFornecedorViaProcedure(@Param("fornecedorId") UUID fornecedorId);

        /**
         * Chama a função 'reativar_fornecedor' no PostgreSQL. Delega a transação
         * atômica de reativar o fornecedor para o banco de dados.
         *
         * @param fornecedorId ID do fornecedor a ser reativado.
         * @pre O fornecedor deve existir e estar desativado.
         * @post O fornecedor é reativado.
         */
        @Modifying
        @Query(value = "CALL reativar_fornecedor(:fornecedorId)", nativeQuery = true)
        void reativarFornecedorViaProcedure(@Param("fornecedorId") UUID fornecedorId);
}
