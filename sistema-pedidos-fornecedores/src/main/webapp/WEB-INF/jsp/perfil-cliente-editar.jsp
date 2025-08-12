<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <!DOCTYPE html>
    <html lang="pt-br">

    <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>Editar Perfil - Sistema de Pedidos</title>
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
        <div class="container profile-container" data-cliente-id="${cliente.id()}">
          <div class="profile-header">
            <h2>Editar Perfil</h2>
          </div>
          <div id="feedback-message" style="display: none;"></div>

          <form id="edit-profile-form">
            <section class="profile-section">
              <h3>Dados Pessoais</h3>
              <div class="form-group">
                <label for="nome">Nome Completo:</label>
                <input type="text" id="nome" name="nome" value="${cliente.nome()}" required>
              </div>
              <div class="form-group">
                <label for="dataNascimento">Data de Nascimento:</label>
                <input type="date" id="dataNascimento" name="dataNascimento" value="${cliente.dataNascimento()}"
                  required>
              </div>
              <div class="form-actions">
                <button type="submit" class="button-link">Salvar Dados Pessoais</button>
                <a href="<c:url value='/cliente/perfil' />" class="button-link-secondary">Cancelar</a>
              </div>
            </section>
          </form>

          <form id="edit-password-form">
            <section class="profile-section">
              <h3>Alterar Senha</h3>
              <div class="form-group">
                <label for="senhaAtual">Senha Atual:</label>
                <input type="password" id="senhaAtual" name="senhaAtual" required>
              </div>
              <div class="form-group">
                <label for="novaSenha">Nova Senha:</label>
                <input type="password" id="novaSenha" name="novaSenha" placeholder="Mínimo 6 caracteres" required>
              </div>
              <div class="form-group">
                <label for="confirmacaoNovaSenha">Confirmar Nova Senha:</label>
                <input type="password" id="confirmacaoNovaSenha" name="confirmacaoNovaSenha" required>
              </div>
              <div class="form-actions">
                <button type="submit" class="button-link">Alterar Senha</button>
              </div>
            </section>
          </form>
        </div>
        <%@ include file="includes/footer.jsp" %>
          <script src="${pageContext.request.contextPath}/js/perfil-cliente-editar.js"></script>
    </body>

    </html>