package br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.utils.enums.StatusPedido;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.pedido.PedidoFornecedorDetalhadoResponseDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.pedido.PedidoRequestDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.pedido.PedidoResponseDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.exception.RegraDeNegocioException;
import jakarta.persistence.EntityNotFoundException;

public interface PedidoUseCases {

  /**
   * Cria um novo pedido para o cliente autenticado.
   * Toda a lógica de validação, cálculo e transação é delegada a uma função no
   * banco de dados.
   *
   * @param requestDTO Os itens do pedido e um código de cupom opcional.
   * @param authUser   O principal de segurança do cliente logado.
   * @return Os dados do pedido recém-criado.
   * @pre O usuário autenticado deve ser um Cliente. O cliente deve ter saldo
   *      suficiente.
   * @post Um pedido e suas transações associadas são salvos no repositório.
   * @throws AccessDeniedException   se o usuário autenticado não for um Cliente.
   * @throws RegraDeNegocioException se alguma regra de negócio for violada
   *                                 (saldo, cupom, produto).
   */
  PedidoResponseDTO criarNovoPedido(PedidoRequestDTO requestDTO, UserDetails authUser);

  /**
   * Lista os pedidos do fornecedor autenticado de forma paginada.
   *
   * @param authUser O principal de segurança do fornecedor logado.
   * @param pageable As informações de paginação.
   * @return Uma página de pedidos do fornecedor.
   * @pre O usuário autenticado deve ser um Fornecedor.
   * @post A lista de pedidos é retornada, podendo estar vazia.
   * @throws AccessDeniedException se o usuário autenticado não for um Fornecedor.
   */
  Page<PedidoFornecedorDetalhadoResponseDTO> listarPedidosDoFornecedor(UserDetails authUser, Pageable pageable);

  /**
   * Busca os detalhes de um pedido específico do fornecedor autenticado.
   *
   * @param pedidoId O ID do pedido a ser buscado.
   * @param authUser O principal de segurança do fornecedor logado.
   * @return Os detalhes do pedido encontrado.
   * @pre O usuário autenticado deve ser um Fornecedor.
   * @post Os detalhes do pedido são retornados.
   * @throws AccessDeniedException se o usuário autenticado não for um Fornecedor.
   * @throws NotFoundException se o pedido não for encontrado.
   */
  PedidoFornecedorDetalhadoResponseDTO buscarDetalhesPedidoFornecedor(UUID pedidoId, UserDetails authUser);

  /**
   * Lista o histórico de pedidos do cliente autenticado de forma paginada.
   *
   * @param authUser O principal de segurança do cliente logado.
   * @param pageable As informações de paginação.
   * @return Uma lista paginada dos pedidos do cliente.
   * @pre O usuário autenticado deve ser um Cliente.
   * @post A lista de pedidos é retornada, podendo estar vazia.
   * @throws AccessDeniedException se o usuário autenticado não for um Cliente.
   */
  Page<PedidoResponseDTO> listarPedidosDoCliente(UserDetails authUser, Pageable pageable);

  /**
   * Lista o histórico de pedidos do cliente autenticado, filtrando por status.
   *
   * @param authUser O principal de segurança do cliente logado.
   * @param status   O status pelo qual os pedidos serão filtrados.
   * @param pageable As informações de paginação.
   * @return Uma lista paginada dos pedidos do cliente que correspondem ao status.
   * @pre O usuário autenticado deve ser um Cliente.
   * @post A lista de pedidos filtrada é retornada.
   * @throws AccessDeniedException se o usuário autenticado não for um Cliente.
   */
  Page<PedidoResponseDTO> listarPedidosDoClientePorStatus(UserDetails authUser, StatusPedido status, Pageable pageable);

  /**
   * Busca um único pedido pelo seu ID, validando se pertence ao cliente
   * autenticado.
   *
   * @param id       O ID do pedido a ser buscado.
   * @param authUser O principal de segurança do cliente logado.
   * @return Os dados do pedido encontrado.
   * @pre O pedido deve existir e pertencer ao cliente autenticado.
   * @post Os dados do pedido são retornados.
   * @throws EntityNotFoundException se o pedido não for encontrado.
   * @throws AccessDeniedException   se o pedido não pertencer ao cliente.
   */
  PedidoResponseDTO buscarPedidoPorId(UUID id, UserDetails authUser);
}