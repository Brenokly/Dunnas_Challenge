package br.com.dunnastecnologia.sistemapedidosfornecedores.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.dunnastecnologia.sistemapedidosfornecedores.model.Cupom;

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
     * Optional vazio é retornado.
     */
    Optional<Cupom> findByCodigo(String codigo);
}
