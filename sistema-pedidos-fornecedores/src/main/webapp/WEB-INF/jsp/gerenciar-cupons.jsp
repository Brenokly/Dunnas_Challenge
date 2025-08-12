<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <!DOCTYPE html>
    <html lang="pt-br">

    <head>
      <meta charset="UTF-8" />
      <meta name="viewport" content="width=device-width, initial-scale=1" />
      <title>Gerenciar Cupons - Fornecedor</title>
      <link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components/forms.css" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components/buttons.css" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components/messages.css" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pages/gerenciar-cupons.css" />
    </head>

    <body>
      <%@ include file="includes/header.jsp" %>
        <div class="dashboard-container" role="main">
          <div class="top-section">
            <h2>Gerenciar Cupons</h2>
            <a href="<c:url value='/fornecedor/cupons/cadastrar' />" class="btn primary-btn">
              Novo Cupom
            </a>
          </div>

          <div id="cupons-container">
            <table class="data-table">
              <thead>
                <tr>
                  <th>Código</th>
                  <th>Valor</th>
                  <th>Tipo</th>
                  <th>Validade</th>
                  <th>Usos (Limite)</th>
                  <th>Status</th>
                  <th>Ações</th>
                </tr>
              </thead>
              <tbody id="cupons-tbody">
              </tbody>
            </table>
            <nav id="paginacao-cupons" class="paginacao" aria-label="Navegação da paginação dos cupons"></nav>
          </div>
        </div>
        <div id="toast-message" class="toast"></div>

        <%@ include file="includes/footer.jsp" %>
          <script src="${pageContext.request.contextPath}/js/gerenciar-cupons.js"></script>
    </body>

    </html>