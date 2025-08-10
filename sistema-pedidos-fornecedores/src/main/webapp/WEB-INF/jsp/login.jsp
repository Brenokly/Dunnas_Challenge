<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

    <jsp:include page="includes/header.jsp" />

    <div class="container">
      <h2>Login</h2>

      <c:if test="${not empty param.error}">
        <div class="error-message">
          Usuário ou senha inválidos. Por favor, tente novamente.
        </div>
      </c:if>

      <form action="<c:url value='/perform_login' />" method="post">
        <div>
          <label for="username">Usuário:</label>
          <input type="text" id="username" name="username" required autofocus>
        </div>
        <div>
          <label for="password">Senha:</label>
          <input type="password" id="password" name="password" required>
        </div>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

        <button type="submit">Entrar</button>
      </form>

      <div class="links" style="text-align: center; margin-top: 1rem;">
        <p>Não tem uma conta? <a href="<c:url value='/cadastro-cliente' />">Cadastre-se como Cliente</a></p>
        <p>Quer vender? <a href="<c:url value='/cadastro-fornecedor' />">Cadastre-se como Fornecedor</a></p>
      </div>
    </div>

    <jsp:include page="includes/footer.jsp" />