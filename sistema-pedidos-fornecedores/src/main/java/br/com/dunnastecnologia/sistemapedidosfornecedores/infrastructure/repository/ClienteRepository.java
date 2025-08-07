package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Cliente;

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
     * @post O cliente é retornado se encontrado, caso contrário, um Optional
     * vazio é retornado.
     */
    Optional<Cliente> findByCpf(String cpf);

    /**
     * Chama a função 'cadastrar_novo_cliente' no PostgreSQL para registrar um
     * novo cliente. Delega a lógica de validação de duplicidade e inserção para
     * o banco de dados.
     *
     * @return o UUID do cliente recém-criado.
     */
    @Query(value = "SELECT cadastrar_novo_cliente(:nome, :cpf, :dataNascimento, :usuario, :senhaHash)", nativeQuery = true)
    UUID registrarClienteViaFuncao(
            @Param("nome") String nome,
            @Param("cpf") String cpf,
            @Param("dataNascimento") LocalDate dataNascimento,
            @Param("usuario") String usuario,
            @Param("senhaHash") String senhaHash
    );

    /**
     * Chama a função 'adicionar_saldo_cliente' no PostgreSQL. Delega a
     * transação atômica de atualizar o saldo e criar o registro no histórico
     * para o banco.
     *
     * @return o novo saldo do cliente após a adição.
     */
    @Query(value = "SELECT adicionar_saldo_cliente(:clienteId, :valor)", nativeQuery = true)
    BigDecimal adicionarSaldoViaFuncao(
            @Param("clienteId") UUID clienteId,
            @Param("valor") BigDecimal valor
    );
}
