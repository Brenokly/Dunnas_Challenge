package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class LimiteDeSaldoException extends RegraDeNegocioException {

  public LimiteDeSaldoException(String message) {
    super(message);
  }
}