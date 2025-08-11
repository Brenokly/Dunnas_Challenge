package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
public class SqlStateErrorMapping {

    private final Map<String, Function<String, ? extends RuntimeException>> errorMap = new HashMap<>();

    public SqlStateErrorMapping() {
        // Erros de Cliente
        errorMap.put("PC001", RecursoDuplicadoException::new);
        errorMap.put("PC002", ContaInativaException::new);
        errorMap.put("PC003", RegraDeNegocioException::new);
        errorMap.put("PC004", LimiteDeSaldoException::new);

        // Erros de Fornecedor
        errorMap.put("PF001", RecursoDuplicadoException::new);
        errorMap.put("PF002", ContaInativaException::new);

        // Erros de Produto
        errorMap.put("P0005", AccessDeniedException::new);

        // Erros de Cupom
        errorMap.put("PCU01", RecursoDuplicadoException::new);
        errorMap.put("PCU02", RegraDeNegocioException::new);
        errorMap.put("PCU03", RegraDeNegocioException::new);
        errorMap.put("PCU04", RegraDeNegocioException::new);

        // ERROS DE PEDIDO
        errorMap.put("PPE01", RegraDeNegocioException::new);
        errorMap.put("PPE02", RegraDeNegocioException::new);

        // Erros de Categoria
        errorMap.put("PCA01", RecursoDuplicadoException::new);
    }

    public Optional<? extends RuntimeException> getExceptionFor(String sqlState, String message) {
        if (errorMap.containsKey(sqlState)) {
            return Optional.of(errorMap.get(sqlState).apply(message));
        }
        return Optional.empty();
    }
}