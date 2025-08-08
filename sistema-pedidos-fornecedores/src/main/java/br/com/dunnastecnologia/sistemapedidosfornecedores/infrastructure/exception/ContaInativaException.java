package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ContaInativaException extends RegraDeNegocioException {

    public ContaInativaException(String message) {
        super(message);
    }
}
