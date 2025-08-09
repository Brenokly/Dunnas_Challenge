package br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.cupom.CupomRequestDTO;
import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.cupom.CupomResponseDTO;
import jakarta.persistence.EntityNotFoundException;

public interface CupomUseCases {
  /**
   * Cria um novo cupom para o fornecedor autenticado.
   *
   * @param requestDTO dados do cupom.
   * @param authUser   O principal de segurança do fornecedor logado.
   * @return dados do cupom criado.
   * @pre O usuário autenticado deve ser um Fornecedor.
   * @post O cupom é salvo no repositório.
   * @throws AccessDeniedException se o usuário não for um Fornecedor.
   */
  CupomResponseDTO criarNovoCupom(CupomRequestDTO requestDTO, UserDetails authUser);

  /**
   * Lista todos os cupons (ativos e inativos) do fornecedor logado.
   *
   * @param authUser O principal de segurança do fornecedor logado.
   * @param pageable informações de paginação.
   * @return uma página dos cupons do fornecedor.
   */
  Page<CupomResponseDTO> listarCuponsDoFornecedor(UserDetails authUser, Pageable pageable);

  /**
   * Busca um cupom por ID, validando a propriedade.
   *
   * @param id       ID do cupom.
   * @param authUser O principal de segurança do fornecedor logado.
   * @return dados do cupom encontrado.
   * @throws EntityNotFoundException se o cupom não for encontrado.
   * @throws AccessDeniedException   se o cupom não pertencer ao fornecedor.
   */
  CupomResponseDTO buscarPorIdDoFornecedor(UUID id, UserDetails authUser);

  /**
   * Desativa um cupom, validando a propriedade.
   *
   * @param id       ID do cupom a ser desativado.
   * @param authUser O principal de segurança do fornecedor logado.
   */
  void desativarCupom(UUID id, UserDetails authUser);

  /**
   * Reativa um cupom, validando a propriedade.
   *
   * @param id       ID do cupom a ser reativado.
   * @param authUser O principal de segurança do fornecedor logado.
   * @return dados do cupom reativado.
   */
  CupomResponseDTO reativarCupom(UUID id, UserDetails authUser);
}