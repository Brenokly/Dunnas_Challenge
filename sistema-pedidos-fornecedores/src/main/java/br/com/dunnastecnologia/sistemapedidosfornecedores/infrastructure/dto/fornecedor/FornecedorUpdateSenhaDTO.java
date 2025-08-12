package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto.fornecedor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FornecedorUpdateSenhaDTO(
    @NotBlank(message = "A senha atual é obrigatória.") String senhaAtual,

    @NotBlank(message = "A nova senha é obrigatória.") @Size(min = 6, message = "A nova senha deve ter no mínimo 6 caracteres.") String novaSenha,

    @NotBlank(message = "A confirmação da nova senha é obrigatória.") String confirmacaoNovaSenha) {
}