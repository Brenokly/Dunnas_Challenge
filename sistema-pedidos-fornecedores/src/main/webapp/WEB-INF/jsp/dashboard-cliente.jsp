<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <!DOCTYPE html>
    <html lang="pt-br">

    <head>
      <meta charset="UTF-8" />
      <meta name="viewport" content="width=device-width, initial-scale=1" />
      <title>Dashboard - Sistema de Pedidos</title>
      <link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components/forms.css" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components/buttons.css" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components/messages.css" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pages/dashboard-cliente.css" />
    </head>

    <body>
      <%@ include file="includes/header.jsp" %>
        <div class="dashboard-container" role="main">
          <%-- REMOVIDA A CONDIÇÃO <c:if test="${userType == 'cliente'}"> --%>
            <div class="top-section">
              <h2>Vitrine de Produtos</h2>
              <button id="btn-filtrar-categorias" aria-haspopup="dialog" aria-expanded="false"
                aria-controls="modal-filtro-categorias" class="primary-btn-icon">
                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none"
                  stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                  class="feather feather-filter">
                  <polygon points="22 3 2 3 10 12.46 10 19 14 21 14 12.46 22 3" />
                </svg>
                <span>Filtrar (0)</span>
              </button>
            </div>
            <section id="produtos-container" aria-live="polite">
              <div id="product-grid" role="list" aria-label="Lista de produtos">
              </div>
            </section>
            <nav id="paginacao-produtos" aria-label="Navegação da paginação dos produtos"></nav>
            <%-- FIM DO CONTEÚDO --%>
        </div>

        <div id="modal-filtro-categorias" class="modal-backdrop" role="dialog" aria-modal="true"
          aria-labelledby="modal-titulo" tabindex="-1">
          <div class="modal-content">
            <h3 id="modal-titulo">Selecione as Categorias</h3>
            <div class="modal-categorias-list" id="modal-categorias-list" tabindex="0">
            </div>
            <div class="modal-buttons">
              <button type="button" class="btn cancel-btn" id="modal-cancel-btn">Cancelar</button>
              <button type="button" class="btn apply-btn" id="modal-apply-btn">Aplicar filtros</button>
            </div>
          </div>
        </div>

        <%@ include file="includes/footer.jsp" %>
          <script src="${pageContext.request.contextPath}/js/dashboard-cliente.js"></script>
    </body>

    </html>