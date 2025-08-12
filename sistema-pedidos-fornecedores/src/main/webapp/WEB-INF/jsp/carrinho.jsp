<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <!DOCTYPE html>
    <html lang="pt-br">

    <head>
      <meta charset="UTF-8" />
      <title>Carrinho - Sistema de Pedidos</title>
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components/forms.css" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components/buttons.css" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components/messages.css" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pages/cart.css" />
    </head>

    <body>
      <%@ include file="includes/header.jsp" %>

        <main class="cart-container">
          <h2>Seu Carrinho</h2>

          <!-- Aqui vai a tabela detalhada dos itens do carrinho -->
          <div id="cart-items-container"></div>

          <!-- Área para mostrar o resumo do pedido: valores, descontos e valor final -->
          <div id="cart-summary-values"
            style="margin-top:1.5rem; font-size:1.1rem; text-align:right; color: var(--text-color);"></div>

          <!-- Botão para finalizar pedido -->
          <div class="cart-actions" style="margin-top:1.5rem;">
            <button id="finalize-order-btn" class="button-link" disabled>Finalizar Pedido</button>
          </div>

          <!-- Feedback geral para erros/sucessos -->
          <div id="feedback-message" class="messages" style="margin-top:1rem;"></div>
        </main>

        <%@ include file="includes/footer.jsp" %>
          <script src="${pageContext.request.contextPath}/js/carrinho.js" defer></script>
    </body>

    </html>