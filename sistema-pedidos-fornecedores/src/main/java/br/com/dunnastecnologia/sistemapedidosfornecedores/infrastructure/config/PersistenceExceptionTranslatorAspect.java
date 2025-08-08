package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.config;

import java.sql.SQLException;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.exception.SqlStateErrorMapping;

@Aspect
@Component
public class PersistenceExceptionTranslatorAspect {

    private final SqlStateErrorMapping errorMapping;

    public PersistenceExceptionTranslatorAspect(SqlStateErrorMapping errorMapping) {
        this.errorMapping = errorMapping;
    }

    @AfterThrowing(
            pointcut = "execution(* br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.repository.*.*(..))",
            throwing = "ex"
    )
    public void translateException(Throwable ex) {
        SQLException rootCause = findRootSQLException(ex);

        if (rootCause != null && rootCause.getSQLState() != null) {
            String sqlState = rootCause.getSQLState();
            String message = extrairMensagemLimpa(rootCause.getMessage());

            errorMapping.getExceptionFor(sqlState, message)
                    .ifPresent(businessException -> {
                        throw businessException;
                    });
        }
    }

    private SQLException findRootSQLException(Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        Throwable rootCause = throwable;
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }
        if (rootCause instanceof SQLException sQLException) {
            return sQLException;
        }
        return null;
    }

    private String extrairMensagemLimpa(String mensagemOriginal) {
        if (mensagemOriginal != null && mensagemOriginal.contains("ERRO:")) {
            return mensagemOriginal.substring(mensagemOriginal.indexOf("ERRO:") + 6).split("\n")[0].trim();
        }
        return "Ocorreu um erro de validação de negócio.";
    }
}
