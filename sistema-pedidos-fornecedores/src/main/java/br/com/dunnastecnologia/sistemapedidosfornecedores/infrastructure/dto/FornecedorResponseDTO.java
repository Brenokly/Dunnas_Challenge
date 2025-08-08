package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto;

import java.util.UUID;

public record FornecedorResponseDTO(
        UUID id,
        String nome,
        String cnpj,
        String usuario,
        Boolean ativo
        ) {

}
