package br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.utils.enums.TipoTransacao;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.transacao.TransacaoResponseDTO;

public interface TransacaoUseCases {

  /**
   * Lista o histórico de transações financeiras do cliente autenticado.
   *
   * @param authUser O principal de segurança do cliente logado.
   * @param pageable As informações de paginação.
   * @return Uma lista paginada das transações do cliente.
   * @pre O usuário autenticado deve ser um Cliente.
   * @post A lista de transações é retornada, podendo estar vazia.
   * @throws AccessDeniedException se o usuário autenticado não for um Cliente.
   */
  Page<TransacaoResponseDTO> listarTransacoesDoCliente(UserDetails authUser, Pageable pageable);

  /**
   * Lista o histórico de transações do cliente autenticado, filtrando por tipo.
   *
   * @param authUser      O principal de segurança do cliente logado.
   * @param tipoTransacao O tipo de transação pelo qual os registros serão
   *                      filtrados.
   * @param pageable      As informações de paginação.
   * @return Uma lista paginada das transações do cliente que correspondem ao
   *         tipo.
   * @pre O usuário autenticado deve ser um Cliente.
   * @post A lista de transações filtrada é retornada.
   * @throws AccessDeniedException se o usuário autenticado não for um Cliente.
   */
  Page<TransacaoResponseDTO> listarTransacoesDoClientePorTipo(UserDetails authUser, TipoTransacao tipoTransacao,
      Pageable pageable);
}