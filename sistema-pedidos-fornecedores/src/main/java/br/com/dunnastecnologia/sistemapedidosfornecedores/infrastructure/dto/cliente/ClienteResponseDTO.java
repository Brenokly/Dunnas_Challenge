package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.cliente;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ClienteResponseDTO(UUID id, String nome, String cpf, LocalDate dataNascimento, String usuario, BigDecimal saldo, Boolean ativo) {

}
