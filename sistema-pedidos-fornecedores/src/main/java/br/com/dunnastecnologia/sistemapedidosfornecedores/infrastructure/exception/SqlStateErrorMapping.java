package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.stereotype.Component;

@Component
public class SqlStateErrorMapping {

    private final Map<String, Function<String, RegraDeNegocioException>> errorMap = new HashMap<>();

    public SqlStateErrorMapping() {
        errorMap.put("PC001", RecursoDuplicadoException::new);
        errorMap.put("PC002", ContaInativaException::new);
        errorMap.put("PC003", RegraDeNegocioException::new);
    }

    public Optional<RegraDeNegocioException> getExceptionFor(String sqlState, String message) {
        if (errorMap.containsKey(sqlState)) {
            return Optional.of(errorMap.get(sqlState).apply(message));
        }
        return Optional.empty();
    }
}
