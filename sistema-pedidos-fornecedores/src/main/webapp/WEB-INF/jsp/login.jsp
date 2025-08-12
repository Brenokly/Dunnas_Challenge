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

          <!-- REMOVIDO: tratamento antigo via parâmetro error -->
          <!-- <c:if test="${not empty param.error}">
      <div class="error-message" style="display:block; color:red; margin-bottom:1rem;">
        Usuário ou senha inválidos. Por favor, tente novamente.
      </div>
    </c:if> -->

          <form id="login-form" autocomplete="off">
            <div class="form-group">
              <label for="username">Usuário:</label>
              <input type="text" id="username" name="username" required autofocus />
            </div>

            <div class="form-group">
              <label for="password">Senha:</label>
              <input type="password" id="password" name="password" required />
            </div>

            <!-- REMOVIDO: CSRF token pois a API REST não usa session -->
            <!-- <input type="hidden" name="_csrf" value="${_csrf.token}" /> -->

            <button type="submit">Entrar</button>
          </form>

          <!-- Div para mostrar erros da autenticação via JS -->
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