package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Cupom;

@Repository
public interface CupomRepository extends JpaRepository<Cupom, UUID> {

    /**
     * Busca um cupom pelo seu código único.
     *
     * @param codigo O código do cupom.
     * @return um Optional contendo o Cupom, se encontrado.
     */
    Optional<Cupom> findByCodigo(String codigo);

    /**
     * Busca todos os cupons de um fornecedor específico.
     *
     * @param fornecedorId O ID do fornecedor.
     * @param pageable     As informações de paginação.
     * @return uma página de cupons do fornecedor.
     */
    Page<Cupom> findAllByFornecedorId(UUID fornecedorId, Pageable pageable);

    /**
     * Chama a função 'cadastrar_novo_cupom' no PostgreSQL.
     *
     * @return o UUID do cupom recém-criado.
     */
    @Query(value = "SELECT cadastrar_novo_cupom(:codigo, :tipo, :valor, :validade, :valorMinimo, :limiteUsos, :fornId)", nativeQuery = true)
    UUID registrarCupomViaFuncao(
            @Param("codigo") String codigo, @Param("tipo") Character tipo, @Param("valor") BigDecimal valor,
            @Param("validade") LocalDate validade, @Param("valorMinimo") BigDecimal valorMinimo,
            @Param("limiteUsos") Integer limiteUsos, @Param("fornId") UUID fornecedorId);

    /**
     * Chama o procedimento 'desativar_cupom' no PostgreSQL.
     *
     * @param cupomId      ID do cupom a ser desativado.
     * @param fornecedorId ID do fornecedor autenticado para validação.
     */
    @Modifying
    @Query(value = "CALL desativar_cupom(:cupomId, :fornecedorId)", nativeQuery = true)
    void desativarCupomViaProcedure(@Param("cupomId") UUID cupomId, @Param("fornecedorId") UUID fornecedorId);

    /**
     * Chama o procedimento 'reativar_cupom' no PostgreSQL.
     *
     * @param cupomId      ID do cupom a ser reativado.
     * @param fornecedorId ID do fornecedor autenticado para validação.
     */
    @Modifying
    @Query(value = "CALL reativar_cupom(:cupomId, :fornecedorId)", nativeQuery = true)
    void reativarCupomViaProcedure(@Param("cupomId") UUID cupomId, @Param("fornecedorId") UUID fornecedorId);
}