<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <!DOCTYPE html>
    <html lang="pt-br">

    <head>
      <meta charset="UTF-8" />
      <meta name="viewport" content="width=device-width, initial-scale=1" />
      <title>Histórico de Transações - Sistema de Pedidos</title>
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components/forms.css">
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components/buttons.css">
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components/messages.css">
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pages/historico.css" />
    </head>

    <body>
      <%@ include file="includes/header.jsp" %>

        <div class="container">
          <h2>Histórico de Transações</h2>

          <nav class="tabs">
            <button class="tab-button active" data-tab="pedidos">Pedidos</button>
            <button class="tab-button" data-tab="transacoes">Transações Financeiras</button>
          </nav>

          <section id="pedidos" class="tab-content active">
            <table id="pedidos-table">
              <thead>
                <tr>
                  <th>ID do Pedido</th>
                  <th>Data</th>
                  <th>Status</th>
                  <th>Valor Total (R$)</th>
                </tr>
              </thead>
              <tbody>
                <!-- Dados carregados via JS -->
              </tbody>
            </table>
            <div id="pedidos-paginacao" class="paginacao"></div>
          </section>

          <section id="transacoes" class="tab-content">
            <table id="transacoes-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Data</th>
                  <th>Tipo</th>
                  <th>Valor (R$)</th>
                </tr>
              </thead>
              <tbody>

              </tbody>
            </table>
            <div id="transacoes-paginacao" class="paginacao"></div>
          </section>
        </div>

        <%@ include file="includes/footer.jsp" %>
          <script src="${pageContext.request.contextPath}/js/historico.js"></script>
    </body>

    </html>