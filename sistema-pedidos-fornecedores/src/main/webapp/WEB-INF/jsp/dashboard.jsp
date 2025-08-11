<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <!DOCTYPE html>
    <html lang="pt-br">

    <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>Dashboard - Sistema de Pedidos</title>
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
      <link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico">
    </head>

    <body>
      <%@ include file="includes/header.jsp" %>

        <div class="container">
          <h2>Bem-vindo ao Dashboard!</h2>
          <p>Você está logado com sucesso.</p>
          <form action="<c:url value='/logout' />" method="post">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            <button type="submit">Sair</button>
          </form>
        </div>
        <%@ include file="includes/footer.jsp" %>

    </body>

    </html>