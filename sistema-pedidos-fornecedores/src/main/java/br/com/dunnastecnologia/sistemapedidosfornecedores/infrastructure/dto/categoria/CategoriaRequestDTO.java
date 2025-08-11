package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaRequestDTO(
    @NotBlank(message = "O nome da categoria não pode ser vazio.")
    @Size(max = 100, message = "O nome da categoria deve ter no máximo 100 caracteres.") String nome) {
}