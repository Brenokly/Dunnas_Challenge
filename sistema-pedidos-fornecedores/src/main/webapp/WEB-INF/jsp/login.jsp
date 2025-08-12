<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <!DOCTYPE html>
    <html lang="pt-br">

    <head>
      <meta charset="UTF-8" />
      <meta name="viewport" content="width=device-width, initial-scale=1.0" />
      <title>Login - Sistema de Pedidos</title>
      <link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico" />

      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components/forms.css" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components/buttons.css" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components/messages.css" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pages/cadastro-login.css" />
    </head>

    <body>

      <%@ include file="includes/header.jsp" %>

        <div class="container">
          <h2>Login</h2>
          <form id="login-form" autocomplete="off">
            <div class="form-group">
              <label for="username">Usuário:</label>
              <input type="text" id="username" name="username" required autofocus />
            </div>

            <div class="form-group">
              <label for="password">Senha:</label>
              <input type="password" id="password" name="password" required />
            </div>

            <button type="submit">Entrar</button>
          </form>

          <div class="error-message" style="display:none; color:red; margin-top:1rem;"></div>

          <div class="links" style="margin-top: 1.5rem;">
            <p>Não tem uma conta? <a href="<c:url value='/cadastro-cliente' />">Cadastre-se como Cliente</a></p>
            <p>Quer vender? <a href="<c:url value='/cadastro-fornecedor' />">Cadastre-se como Fornecedor</a></p>
          </div>
        </div>

        <%@ include file="includes/footer.jsp" %>

          <script src="${pageContext.request.contextPath}/js/login.js" defer></script>
    </body>

    </html>