package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FornecedorRequestDTO(
        @NotBlank
        String nome,
        @NotBlank
        @Size(min = 14, max = 14)
        String cnpj,
        @NotBlank
        String usuario,
        @NotBlank
        @Size(min = 6)
        String senha
        ) {

}
