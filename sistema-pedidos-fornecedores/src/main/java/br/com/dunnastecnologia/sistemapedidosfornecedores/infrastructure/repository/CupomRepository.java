package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Cupom;

@Repository
public interface CupomRepository extends JpaRepository<Cupom, UUID> {

    /**
     * Busca um cupom pelo seu código único. Este método será usado no ato da
     * compra para validar e aplicar um cupom de desconto.
     *
     * @param codigo O código do cupom a ser buscado (ex: "PROMO10").
     * @return um Optional contendo o Cupom, se encontrado.
     * @pre O código não deve ser nulo.
     * @post O cupom é retornado se o código existir, caso contrário, um
     *       Optional vazio é retornado.
     */
    Optional<Cupom> findByCodigo(String codigo);

    @Query(value = "SELECT cadastrar_novo_cupom(:codigo, :tipo, :valor, :validade)", nativeQuery = true)
    UUID registrarCupomViaFuncao(
            @Param("codigo") String codigo, @Param("tipo") Character tipo,
            @Param("valor") BigDecimal valor, @Param("validade") LocalDate validade);
}
