package br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases;

import java.util.UUID;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.cliente.ClienteRequestDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.cliente.ClienteResponseDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.cliente.ClienteUpdateRequestDTO;
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
     * Busca os dados do cliente LOGADO.
     *
     * @param authUser O principal de segurança do usuário logado.
     * @return dados do cliente logado.
     * @throws EntityNotFoundException se o cliente não for encontrado.
     * @throws AccessDeniedException   se o usuário logado não for um cliente.
     */
    ClienteResponseDTO buscarClienteLogado(UserDetails authUser);

    /**
     * Atualiza os dados de um cliente, validando a propriedade.
     *
     * @param id         O ID do cliente a ser atualizado.
     * @param requestDTO Os dados do cliente a serem atualizados (campos opcionais).
     * @param authUser   O principal de segurança do usuário logado.
     * @return Os dados do cliente atualizado.
     * @pre O cliente deve existir e pertencer ao usuário autenticado.
     * @post Os dados do cliente são atualizados no repositório.
     * @throws AccessDeniedException   se o usuário não for o proprietário.
     * @throws EntityNotFoundException se o cliente não for encontrado.
     */
    ClienteResponseDTO atualizarCliente(UUID id, ClienteUpdateRequestDTO requestDTO, UserDetails authUser);

    /**
     * Adiciona saldo à conta do cliente LOGADO.
     *
     * @param authUser O principal de segurança do usuário logado.
     * @param valorDTO dados do valor a ser adicionado.
     * @return dados do cliente atualizado.
     * @throws EntityNotFoundException se o cliente não for encontrado.
     * @throws AccessDeniedException   se o usuário logado não for um cliente.
     * @throws RegraDeNegocioException se alguma regra de negócio for violada.
     */
    ClienteResponseDTO adicionarSaldo(UserDetails authUser, ValorRequestDTO valorDTO);

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

}
