<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <!DOCTYPE html>
    <html lang="pt-br">

    <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>Meu Perfil - Fornecedor</title>
      <link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico">

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
            <a href="<c:url value='/fornecedor/perfil/editar' />" class="button-link-secondary">Editar Perfil</a>
          </div>

          <div id="feedback-message" style="display: none;"></div>

          <section class="profile-section">
            <h3>Dados da Empresa</h3>
            <div class="profile-details">
              <div class="detail-item"><span class="label">Nome da Empresa:</span><span id="nome-perfil"
                  class="value">${fornecedor.nome()}</span></div>
              <div class="detail-item"><span class="label">Usuário:</span><span id="usuario-perfil"
                  class="value">${fornecedor.usuario()}</span></div>
              <div class="detail-item"><span class="label">CNPJ:</span><span id="cnpj-perfil"
                  class="value">${fornecedor.cnpj()}</span></div>
            </div>
          </section>

          <section class="profile-section danger-zone">
            <h3>Área de Risco</h3>
            <p>A desativação da sua conta fará com que você não possa mais logar ou gerenciar seus produtos.</p>
            <form id="delete-account-form" data-fornecedor-id="${fornecedor.id()}">
              <button type="submit" class="button-link-danger">Desativar Minha Conta</button>
            </form>
          </section>
        </div>

        <%@ include file="includes/footer.jsp" %>
          <script src="${pageContext.request.contextPath}/js/perfil-fornecedor.js"></script>
    </body>

    </html>