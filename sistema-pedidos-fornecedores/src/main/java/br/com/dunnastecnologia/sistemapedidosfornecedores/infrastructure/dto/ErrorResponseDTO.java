package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.dto;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        String message,
        int statusCode,
        LocalDateTime timestamp
        ) {

}
