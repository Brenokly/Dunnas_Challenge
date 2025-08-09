package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.cupom;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CupomRequestDTO(
    @NotBlank String codigo,
    @NotNull Character tipoDesconto,
    @NotNull @Positive BigDecimal valor,
    @NotNull @FutureOrPresent LocalDate dataValidade) {
}