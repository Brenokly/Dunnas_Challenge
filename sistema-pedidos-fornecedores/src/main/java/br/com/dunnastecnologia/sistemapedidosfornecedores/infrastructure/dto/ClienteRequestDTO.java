package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto;

import java.time.LocalDate;

public record ClienteRequestDTO(String nome, String cpf, LocalDate dataNascimento, String usuario, String senha) {

}
