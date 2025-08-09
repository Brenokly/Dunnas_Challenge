package br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.cliente.ClienteRequestDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.cliente.ClienteResponseDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.cliente.ValorRequestDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.exception.RegraDeNegocioException;
import jakarta.persistence.EntityNotFoundException;

public interface ClienteUseCases {

    /**
     * Cadastra um novo cliente.
     *
     * @param requestDTO dados do cliente a ser cadastrado.
     * @return dados do cliente cadastrado.
     * @pre O cliente não deve existir.
     * @post O cliente é salvo no repositório.
     * @throws RegraDeNegocioException se alguma regra de negócio for violada ou
     *                                 IllegalStateException caso ocorra algum erro
     *                                 inesperado.
     */
    ClienteResponseDTO cadastrarNovoCliente(ClienteRequestDTO requestDTO);

    /**
     * Busca um cliente por ID.
     *
     * @param id ID do cliente a ser buscado.
     * @return descrição do retorno.
     * @pre O cliente deve existir.
     * @post O cliente é retornado como ClienteResponseDTO.
     * @throws EntityNotFoundException se o cliente não for encontrado.
     */
    ClienteResponseDTO buscarPorId(UUID id);

    /**
     * Lista todos os clientes.
     *
     * @param pageable informações de paginação.
     * @return lista de clientes paginada.
     * @pre Nenhuma.
     * @post A lista de clientes é retornada.
     */
    Page<ClienteResponseDTO> listarTodos(Pageable pageable);

    /**
     * Adiciona saldo a um cliente.
     *
     * @param id       ID do cliente.
     * @param valorDTO dados do valor a ser adicionado.
     * @return dados do cliente atualizado.
     * @pre O cliente deve existir.
     * @post O saldo do cliente é atualizado.
     * @throws EntityNotFoundException se o cliente não for encontrado ou
     *                                 RegraDeNegocioException se alguma regra de
     *                                 negócio for violada.
     */
    ClienteResponseDTO adicionarSaldo(UUID id, ValorRequestDTO valorDTO);

    /**
     * Desativa um cliente, validando a propriedade.
     *
     * @param id       ID do cliente a ser desativado.
     * @param authUser O principal de segurança do usuário logado.
     * @pre O cliente deve existir e pertencer ao usuário autenticado.
     * @post O cliente é desativado.
     * @throws EntityNotFoundException se o cliente não for encontrado.
     * @throws AccessDeniedException   se o usuário não for o proprietário.
     */
    void desativarCliente(UUID id, UserDetails authUser);

    /**
     * Reativa um cliente, validando a propriedade.
     *
     * @param id       ID do cliente a ser reativado.
     * @param authUser O principal de segurança do usuário logado.
     * @return dados do cliente reativado.
     * @pre O cliente deve existir e pertencer ao usuário autenticado.
     * @post O cliente é reativado.
     * @throws EntityNotFoundException se o cliente não for encontrado.
     * @throws AccessDeniedException   se o usuário não for o proprietário.
     */
    ClienteResponseDTO reativarCliente(UUID id, UserDetails authUser);
}
