package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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
     * @param pageable O objeto contendo as informações de paginação.
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
     * @param status O status do pedido a ser filtrado.
     * @param pageable O objeto contendo as informações de paginação.
     * @return uma Página (Page) de Pedidos que correspondem aos critérios.
     * @pre O ID do cliente e o status devem ser válidos.
     * @post Uma página de pedidos é retornada, podendo estar vazia.
     */
    Page<Pedido> findAllByClienteIdAndStatus(UUID clienteId, StatusPedido status, Pageable pageable);
}
