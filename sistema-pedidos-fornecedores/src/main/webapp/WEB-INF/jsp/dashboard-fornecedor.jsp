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
          <p>Bem-vindo ao seu painel de controle. Aqui você poderá gerenciar seus produtos e pedidos.</p>

          <section class="fornecedor-painel">
            <div class="card-metricas">
              <div class="card">
                <h4>Total de Pedidos Pendentes</h4>
                <p class="valor">15</p>
              </div>
              <div class="card">
                <h4>Produtos Cadastrados</h4>
                <p class="valor">32</p>
              </div>
              <div class="card">
                <h4>Pedidos Finalizados</h4>
                <p class="valor">145</p>
              </div>
            </div>

            <h3>Últimos Pedidos</h3>
            <table class="data-table">
              <thead>
                <tr>
                  <th>ID Pedido</th>
                  <th>Cliente</th>
                  <th>Status</th>
                  <th>Valor Total</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>#12345</td>
                  <td>João da Silva</td>
                  <td><span class="status-pendente">Pendente</span></td>
                  <td>R$ 150,00</td>
                </tr>
                <tr>
                  <td>#12344</td>
                  <td>Maria Oliveira</td>
                  <td><span class="status-finalizado">Finalizado</span></td>
                  <td>R$ 230,50</td>
                </tr>
              </tbody>
            </table>
          </section>

        </div>
        <%@ include file="includes/footer.jsp" %>
          <script src="${pageContext.request.contextPath}/js/dashboard-fornecedor.js"></script>
    </body>

    </html>