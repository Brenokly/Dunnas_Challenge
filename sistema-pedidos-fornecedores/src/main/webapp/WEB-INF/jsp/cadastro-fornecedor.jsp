<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <!DOCTYPE html>
    <html lang="pt-br">

    <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>Cadastro de Fornecedor - Sistema de Pedidos</title>
      <link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico">

      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components/forms.css">
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components/buttons.css">
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components/messages.css">
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pages/cadastro-login.css">
    </head>

    <body>

      <%@ include file="includes/header.jsp" %>

        <div class="container">
          <h2>Cadastro de Novo Fornecedor</h2>

          <div id="feedback-message" style="display: none;"></div>

          <form id="cadastro-fornecedor-form">
            <div class="form-group">
              <label for="nome">Nome da Empresa:</label>
              <input type="text" id="nome" name="nome" required>
            </div>
            <div class="form-group">
              <label for="cnpj">CNPJ (apenas números):</label>
              <input type="text" id="cnpj" name="cnpj" required maxlength="18">
            </div>
            <div class="form-group">
              <label for="usuario">Usuário:</label>
              <input type="text" id="usuario" name="usuario" required>
            </div>
            <div class="form-group">
              <label for="senha">Senha:</label>
              <input type="password" id="senha" name="senha" required>
            </div>

            <button type="submit">Cadastrar</button>
          </form>

          <div class="links">
            <p>Já tem uma conta? <a href="<c:url value='/login' />">Faça o login</a></p>
          </div>
        </div>

        <%@ include file="includes/footer.jsp" %>

          <script src="${pageContext.request.contextPath}/js/cadastro-fornecedor.js"></script>

    </body>

    </html>