package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.categoria;

import java.util.UUID;

public record CategoriaResponseDTO(
    UUID id,
    String nome,
    Boolean ativo) {
}
