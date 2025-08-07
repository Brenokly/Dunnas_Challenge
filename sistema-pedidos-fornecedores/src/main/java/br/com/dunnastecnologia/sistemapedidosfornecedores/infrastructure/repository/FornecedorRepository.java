package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Fornecedor;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, UUID> {

    /**
     * Busca um fornecedor pelo seu nome de usuário.
     *
     * @param usuario O nome de usuário a ser buscado.
     * @return um Optional contendo o Fornecedor, se encontrado.
     * @pre O fornecedor deve existir no sistema.
     * @post O fornecedor deve ser retornado com todos os seus dados.
     */
    Optional<Fornecedor> findByUsuario(String usuario);

    /**
     * Busca um fornecedor pelo seu CNPJ. Útil para validar se um CNPJ já foi
     * cadastrado.
     *
     * @param cnpj O CNPJ a ser buscado.
     * @return um Optional contendo o Fornecedor, se encontrado.
     * @pre O fornecedor deve existir no sistema.
     * @post O fornecedor deve ser retornado com todos os seus dados.
     */
    Optional<Fornecedor> findByCnpj(String cnpj);
}
