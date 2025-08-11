<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <!DOCTYPE html>
    <html lang="pt-br">

    <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>Meu Perfil - Sistema de Pedidos</title>
      <link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico">

      <%-- Imports do CSS Modular --%>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components/forms.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components/buttons.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components/messages.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pages/perfil.css">
    </head>

    <body>
      <%@ include file="includes/header.jsp" %>

        <div class="profile-container">
          <div class="profile-header">
            <h2>Meu Perfil</h2>
            <a href="<c:url value='/cliente/perfil/editar' />" class="button-link-secondary">Editar Perfil</a>
          </div>

          <div id="feedback-message" style="display: none;"></div>

          <section class="profile-section">
            <h3>Dados Pessoais</h3>
            <div class="profile-details">
              <div class="detail-item"><span class="label">Nome:</span><span class="value">${cliente.nome()}</span>
              </div>
              <div class="detail-item"><span class="label">Usuário:</span><span
                  class="value">${cliente.usuario()}</span></div>
              <div class="detail-item"><span class="label">CPF:</span><span class="value">${cliente.cpf()}</span></div>
              <div class="detail-item"><span class="label">Data de Nascimento:</span><span
                  class="value">${cliente.dataNascimento()}</span></div>
            </div>
          </section>

          <section class="profile-section">
            <h3>Gestão de Saldo</h3>
            <div class="profile-details">
              <div class="detail-item">
                <span class="label">Saldo em Conta:</span>
                <span id="saldo-valor" class="value balance">R$ ${String.format("%.2f", cliente.saldo())}</span>
              </div>
            </div>
            <form id="add-saldo-form" class="saldo-form">
              <div class="form-group">
                <label for="valor">Adicionar Saldo:</label>
                <input type="number" id="valor" name="valor" placeholder="Ex: 50.00" step="0.01" min="0.01" required>
              </div>
              <button type="submit">Adicionar</button>
            </form>
          </section>

          <section class="profile-section danger-zone">
            <h3>Área de Risco</h3>
            <p>A desativação da sua conta fará com que você não possa mais logar ou fazer novos pedidos.</p>
            <form id="delete-account-form">
              <button type="submit" class="button-link-danger">Desativar Minha Conta</button>
            </form>
          </section>
        </div>

        <%@ include file="includes/footer.jsp" %>
          <script src="${pageContext.request.contextPath}/js/perfil-cliente.js"></script>
    </body>

    </html>