package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.ItensPedido;
import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.ItensPedidoId;

public interface ItensPedidoRepository extends JpaRepository<ItensPedido, ItensPedidoId> {

  @Query("SELECT ip FROM ItensPedido ip JOIN ip.produto p WHERE p.fornecedor.id = :fornecedorId ORDER BY ip.pedido.dataPedido DESC")
  Page<ItensPedido> findByProdutoFornecedorId(@Param("fornecedorId") UUID fornecedorId, Pageable pageable);

}