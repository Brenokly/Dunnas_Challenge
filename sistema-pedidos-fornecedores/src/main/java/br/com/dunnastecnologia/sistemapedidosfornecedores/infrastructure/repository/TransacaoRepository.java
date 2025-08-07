package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Transacao;
import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.utils.enums.TipoTransacao;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, UUID> {

    /**
     * Busca uma página do histórico de transações de um cliente específico.
     *
     * @param clienteId O ID do cliente.
     * @param pageable O objeto contendo as informações de paginação.
     * @return uma Página (Page) com o histórico de Transações do cliente.
     * @pre O ID do cliente deve ser válido.
     * @post Uma página de transações é retornada, podendo estar vazia.
     */
    Page<Transacao> findAllByClienteId(UUID clienteId, Pageable pageable);

    /**
     * Busca uma página do histórico de transações de um cliente, filtrada por
     * tipo.
     *
     * @param clienteId O ID do cliente.
     * @param tipoTransacao O tipo de transação a ser filtrado.
     * @param pageable O objeto contendo as informações de paginação.
     * @return uma Página (Page) com o histórico de Transações do cliente que
     * corresponde ao tipo.
     * @pre O ID do cliente e o tipo de transação devem ser válidos.
     * @post Uma página de transações é retornada, podendo estar vazia.
     */
    Page<Transacao> findAllByClienteIdAndTipoTransacao(UUID clienteId, TipoTransacao tipoTransacao, Pageable pageable);
}
