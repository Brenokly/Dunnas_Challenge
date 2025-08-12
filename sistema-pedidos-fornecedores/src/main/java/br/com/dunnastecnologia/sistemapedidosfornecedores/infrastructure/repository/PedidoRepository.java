package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Pedido;
import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.utils.enums.StatusPedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, UUID> {

    /**
     * Busca uma página de pedidos de um cliente específico. Essencial para a
     * funcionalidade de "visualização do histórico de transações".
     *
     * @param clienteId O ID do cliente cujos pedidos serão buscados.
     * @param pageable  O objeto contendo as informações de paginação.
     * @return uma Página (Page) de Pedidos do cliente.
     * @pre O ID do cliente deve ser válido.
     * @post Uma página de pedidos é retornada, podendo estar vazia.
     */
    Page<Pedido> findAllByClienteId(UUID clienteId, Pageable pageable);

    /**
     * Busca uma página de pedidos de um cliente específico com um determinado
     * status.
     *
     * @param clienteId O ID do cliente.
     * @param status    O status do pedido a ser filtrado.
     * @param pageable  O objeto contendo as informações de paginação.
     * @return uma Página (Page) de Pedidos que correspondem aos critérios.
     * @pre O ID do cliente e o status devem ser válidos.
     * @post Uma página de pedidos é retornada, podendo estar vazia.
     */
    Page<Pedido> findAllByClienteIdAndStatus(UUID clienteId, StatusPedido status, Pageable pageable);

    /**
     * Chama a função 'realizar_pedido' no PostgreSQL, que executa toda a transação
     * de negócio.
     *
     * @param clienteId   ID do cliente que está fazendo o pedido.
     * @param itensJson   String JSON contendo a lista de produtos e quantidades.
     * @param codigoCupom Código do cupom a ser aplicado (pode ser nulo ou vazio).
     * @return o UUID do pedido recém-criado.
     * @pre O cliente deve existir, ter saldo e os produtos devem ser válidos.
     * @post Um novo pedido e suas transações associadas são criados no banco de
     *       dados.
     * @throws RuntimeException se qualquer regra de negócio (saldo, estoque, cupom)
     *                          for violada.
     */
    @Query(value = "SELECT realizar_pedido(:clienteId, CAST(:itensJson AS jsonb), :codigoCupom)", nativeQuery = true)
    UUID registrarPedidoViaFuncao(
            @Param("clienteId") UUID clienteId,
            @Param("itensJson") String itensJson,
            @Param("codigoCupom") String codigoCupom);
}
