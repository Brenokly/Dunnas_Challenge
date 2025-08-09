package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.transacao;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.utils.enums.TipoTransacao;

public record TransacaoResponseDTO(
    UUID id,
    OffsetDateTime dataTransacao,
    TipoTransacao tipoTransacao,
    BigDecimal valor,
    UUID clienteId,
    UUID pedidoId // Pode ser nulo caso seja, por exemplo, uma adição de saldo de um cliente.
) {
}