<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <!DOCTYPE html>
    <html lang="pt-br">

    <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>Sistema de Pedidos - Dunnas</title>
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
      <link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico">
    </head>

    <body>
      <%@ include file="includes/header.jsp" %>

        <div class="container">
          <h2>Login</h2>

          <c:if test="${not empty param.error}">
            <div class="error-message">
              Usuário ou senha inválidos. Por favor, tente novamente.
            </div>
          </c:if>

          <form action="<c:url value='/perform_login' />" method="post">
            <div class="form-group">
              <label for="username">Usuário:</label>
              <input type="text" id="username" name="username" required autofocus>
            </div>
            <div class="form-group">
              <label for="password">Senha:</label>
              <input type="password" id="password" name="password" required>
            </div>

            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

            <button type="submit">Entrar</button>
          </form>

          <div class="links">
            <p>Não tem uma conta? <a href="<c:url value='/cadastro-cliente' />">Cadastre-se como Cliente</a></p>
            <p>Quer vender? <a href="<c:url value='/cadastro-fornecedor' />">Cadastre-se como Fornecedor</a></p>
          </div>
        </div>

        <%@ include file="includes/footer.jsp" %>

    </body>

    </html>