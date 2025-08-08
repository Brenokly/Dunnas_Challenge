package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.config;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.exception.RegraDeNegocioException;

@Aspect
@Component
public class PersistenceExceptionTranslatorAspect {

    @AfterThrowing(
            pointcut = "execution(* br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.repository.*.*(..))",
            throwing = "ex"
    )
    public void translateException(Throwable ex) {
        String rootCauseMessage = getRootCauseMessage(ex);

        if (rootCauseMessage != null && rootCauseMessage.contains("Regra de Negócio Violada")) {
            String cleanMessage = extrairMensagemLimpa(rootCauseMessage);
            throw new RegraDeNegocioException(cleanMessage);
        }
    }

    private String getRootCauseMessage(Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        Throwable rootCause = throwable;
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }
        return rootCause.getMessage();
    }

    private String extrairMensagemLimpa(String mensagemOriginal) {
        if (mensagemOriginal.contains("ERRO:")) {
            return mensagemOriginal.substring(mensagemOriginal.indexOf("ERRO:") + 6).split("\n")[0].trim();
        }
        return "Ocorreu um erro de validação de negócio.";
    }
}
