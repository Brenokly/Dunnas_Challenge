package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.cupom;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CupomResponseDTO(
        UUID id, String codigo, Character tipoDesconto, BigDecimal valor,
        LocalDate dataValidade, BigDecimal valorMinimoPedido,
        Integer limiteDeUsos, Integer usosAtuais,
        Boolean ativo, UUID fornecedorId) {
}