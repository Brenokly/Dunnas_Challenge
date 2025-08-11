<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <!DOCTYPE html>
    <html lang="pt-br">

    <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>Meu Perfil - Sistema de Pedidos</title>
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
      <link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico">
    </head>

    <body>

      <%@ include file="includes/header.jsp" %>

        <div class="container profile-container">
          <h2>Meu Perfil - Cliente</h2>

          <div class="profile-details">
            <div class="detail-item">
              <span class="label">Nome:</span>
              <span class="value">${cliente.nome()}</span>
            </div>
            <div class="detail-item">
              <span class="label">Usuário:</span>
              <span class="value">${cliente.usuario()}</span>
            </div>
            <div class="detail-item">
              <span class="label">CPF:</span>
              <span class="value">${cliente.cpf()}</span>
            </div>
            <div class="detail-item">
              <span class="label">Data de Nascimento:</span>
              <span class="value">${cliente.dataNascimento()}</span>
            </div>
            <div class="detail-item">
              <span class="label">Saldo em Conta:</span>
              <span class="value balance">R$ ${cliente.saldo()}</span>
            </div>
          </div>

          <div class="profile-actions">
            <a href="#" class="button-link">Adicionar Saldo</a>
            <a href="#" class="button-link-danger">Desativar Conta</a>
          </div>
        </div>

        <%@ include file="includes/footer.jsp" %>

    </body>

    </html>