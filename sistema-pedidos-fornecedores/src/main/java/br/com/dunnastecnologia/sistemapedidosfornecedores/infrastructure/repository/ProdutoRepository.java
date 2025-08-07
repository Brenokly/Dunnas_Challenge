package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, UUID> {

    /**
     * Busca uma página de produtos pertencentes a um fornecedor específico.
     *
     * @param fornecedorId O ID do fornecedor.
     * @param pageable O objeto contendo as informações de paginação (página,
     * tamanho, ordenação).
     * @return uma Página (Page) de Produtos contendo os itens da página atual e
     * metadados de paginação.
     * @pre O ID do fornecedor deve ser válido.
     * @post Uma página de produtos é retornada, podendo estar vazia.
     */
    Page<Produto> findAllByFornecedorId(UUID fornecedorId, Pageable pageable);

    /**
     * Busca produtos cujo nome contém o termo de busca, ignorando maiúsculas e
     * minúsculas.
     *
     * @param nome O termo a ser buscado no nome dos produtos.
     * @param pageable O objeto contendo as informações de paginação.
     * @return uma Página (Page) de Produtos que correspondem ao critério de
     * busca.
     * @pre O termo de busca não deve ser nulo.
     * @post Uma página de produtos correspondentes é retornada, podendo estar
     * vazia.
     */
    Page<Produto> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
