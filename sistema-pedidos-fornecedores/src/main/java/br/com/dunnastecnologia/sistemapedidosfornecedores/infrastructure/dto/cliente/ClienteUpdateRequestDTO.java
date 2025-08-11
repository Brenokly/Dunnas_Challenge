package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.cliente;

import java.time.LocalDate;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public record ClienteUpdateRequestDTO(
    @Size(min = 1, message = "O nome não pode ser vazio.") String nome,

    @Past(message = "A data de nascimento deve ser no passado.") LocalDate dataNascimento,

    @Size(min = 6, message = "A nova senha deve ter no mínimo 6 caracteres.") String senha) {
}