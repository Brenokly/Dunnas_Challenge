package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class SaldoInsuficienteException extends RegraDeNegocioException {

    public SaldoInsuficienteException(String message) {
        super(message);
    }
}
