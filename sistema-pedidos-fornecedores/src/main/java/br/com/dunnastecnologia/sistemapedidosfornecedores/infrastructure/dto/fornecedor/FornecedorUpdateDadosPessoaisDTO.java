package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.fornecedor;

import jakarta.validation.constraints.Size;

public record FornecedorUpdateDadosPessoaisDTO(
        @Size(min = 1, message = "O nome não pode ser vazio.") String nome) {
}
