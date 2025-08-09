package br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases;

import java.nio.file.AccessDeniedException;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.fornecedor.FornecedorRequestDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.fornecedor.FornecedorResponseDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.exception.RegraDeNegocioException;
import jakarta.persistence.EntityNotFoundException;

public interface FornecedorUseCases {

    /**
     * Cadastra um novo fornecedor.
     *
     * @param requestDTO dados do fornecedor a ser cadastrado.
     * @return dados do fornecedor cadastrado.
     * @pre O fornecedor não deve existir.
     * @post O fornecedor é salvo no repositório.
     * @throws RegraDeNegocioException se alguma regra de negócio for violada ou
     *                                 IllegalStateException caso ocorra algum erro
     *                                 inesperado.
     */
    FornecedorResponseDTO cadastrarNovoFornecedor(FornecedorRequestDTO requestDTO);

    /**
     * Lista todos os fornecedores.
     *
     * @param pageable informações de paginação.
     * @return lista de fornecedores paginada.
     * @pre Nenhuma.
     * @post A lista de fornecedores é retornada.
     */
    Page<FornecedorResponseDTO> listarTodos(Pageable pageable);

    /**
     * Busca um fornecedor pelo ID.
     *
     * @param id ID do fornecedor a ser buscado.
     * @return dados do fornecedor encontrado.
     * @pre O fornecedor deve existir.
     * @post Os dados do fornecedor são retornados.
     * @throws EntityNotFoundException se o fornecedor não for encontrado.
     */
    FornecedorResponseDTO buscarPorId(UUID id);

    /**
     * Desativa um fornecedor, validando a propriedade.
     *
     * @param id       ID do fornecedor a ser desativado.
     * @param authUser O principal de segurança do usuário logado.
     * @pre O fornecedor deve existir e pertencer ao usuário autenticado.
     * @post O fornecedor é desativado.
     * @throws EntityNotFoundException se o fornecedor não for encontrado.
     * @throws AccessDeniedException   se o usuário não for o proprietário.
     */
    void desativarFornecedor(UUID id, UserDetails authUser);

    /**
     * Reativa um fornecedor, validando a propriedade.
     *
     * @param id       ID do fornecedor a ser reativado.
     * @param authUser O principal de segurança do usuário logado.
     * @return dados do fornecedor reativado.
     * @pre O fornecedor deve existir e pertencer ao usuário autenticado.
     * @post O fornecedor é reativado.
     * @throws EntityNotFoundException se o fornecedor não for encontrado.
     * @throws AccessDeniedException   se o usuário não for o proprietário.
     */
    FornecedorResponseDTO reativarFornecedor(UUID id, UserDetails authUser);
}
