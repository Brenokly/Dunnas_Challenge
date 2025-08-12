<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <!DOCTYPE html>
    <html lang="pt-br">

    <head>
      <meta charset="UTF-8" />
      <meta name="viewport" content="width=device-width, initial-scale=1" />
      <title>Dashboard - Fornecedor</title>
      <link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components/forms.css" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components/buttons.css" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components/messages.css" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pages/dashboard-fornecedor.css" />
    </head>

    <body>
      <%@ include file="includes/header.jsp" %>
        <div class="dashboard-container" role="main">
          <h2>Dashboard do Fornecedor</h2>
          <p>Gerencie seus produtos e pedidos de forma eficiente.</p>

          <section class="fornecedor-painel">

            <div class="tabs">
              <button class="tab-button active" data-tab="produtos">Meus Produtos</button>
              <button class="tab-button" data-tab="pedidos">Meus Pedidos</button>
            </div>

            <section id="produtos" class="tab-content active">
              <div class="top-section">
                <h3>Lista de Produtos</h3>
                <div class="top-actions">
                  <a href="<c:url value='/fornecedor/cupons' />" class="btn primary-btn">
                    Gerenciar Cupons
                  </a>
                  <a href="<c:url value='/fornecedor/produtos/cadastrar' />" class="btn primary-btn">
                    Adicionar Produto
                  </a>
                </div>
              </div>
              <div class="product-grid-wrapper">
                <div id="product-grid" role="list" aria-label="Lista de produtos">
                </div>
              </div>
              <nav id="paginacao-produtos" class="paginacao" aria-label="Navegação da paginação dos produtos"></nav>
            </section>

            <section id="pedidos" class="tab-content">
              <div class="header-content">
                <h3>Pedidos Recentes</h3>
              </div>
              <table class="data-table">
                <thead>
                  <tr>
                    <th>ID Pedido</th>
                    <th>Cliente</th>
                    <th>Data</th>
                    <th>Status</th>
                    <th>Ações</th>
                  </tr>
                </thead>
                <tbody id="pedidos-tbody">
                </tbody>
              </table>
              <nav id="paginacao-pedidos" class="paginacao" aria-label="Navegação da paginação dos pedidos"></nav>
            </section>
          </section>

        </div>

        <div id="modal-detalhes-pedido" class="modal-backdrop" role="dialog" aria-modal="true"
          aria-labelledby="modal-detalhes-titulo" tabindex="-1">
          <div class="modal-content">
            <div class="modal-header">
              <h3 id="modal-detalhes-titulo">Detalhes do Pedido</h3>
              <button class="modal-close-btn">&times;</button>
            </div>
            <div id="modal-detalhes-body">
            </div>
            <div class="modal-footer">
              <button class="btn secondary-btn modal-close-btn">Fechar</button>
            </div>
          </div>
        </div>

        <%@ include file="includes/footer.jsp" %>
          <script src="${pageContext.request.contextPath}/js/dashboard-fornecedor.js"></script>
    </body>

    </html>