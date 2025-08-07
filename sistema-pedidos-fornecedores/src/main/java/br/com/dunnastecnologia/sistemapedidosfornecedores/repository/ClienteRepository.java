package br.com.dunnastecnologia.sistemapedidosfornecedores.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.dunnastecnologia.sistemapedidosfornecedores.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

    /**
     * Busca um cliente pelo seu nome de usuário.
     *
     * @param usuario O nome de usuário a ser buscado.
     * @return um Optional contendo o Cliente, se encontrado.
     * @pre O cliente deve existir no sistema.
     * @post O cliente é retornado se encontrado, caso contrário, um Optional
     * vazio é retornado.
     */
    Optional<Cliente> findByUsuario(String usuario);

    /**
     * Busca um cliente pelo seu CPF.
     *
     * @param cpf O CPF a ser buscado.
     * @return um Optional contendo o Cliente, se encontrado.
     * @pre O cliente deve existir no sistema.
     * @post O cliente é retornado se encontrado, caso contrário, um Optional vazio é retornado.
     */
    Optional<Cliente> findByCpf(String cpf);
}
