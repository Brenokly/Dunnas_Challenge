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

          <!-- Campo para inserção do cupom e botão para aplicar -->
          <div class="cart-summary" style="margin-top:1rem;">
            <label for="coupon-code">Cupom de desconto (opcional):</label><br />
            <input type="text" id="coupon-code" name="couponCode" placeholder="Insira o código do cupom" />
            <button id="apply-coupon-btn" class="button-link">Aplicar Cupom</button>
            <div id="coupon-feedback" class="coupon-feedback" style="margin-top:0.5rem;"></div>
          </div>

          <!-- Botão para finalizar pedido -->
          <div class="cart-actions" style="margin-top:1.5rem;">
            <button id="finalize-order-btn" class="button-link" disabled>Finalizar Pedido</button>
          </div>

          <!-- Feedback geral para erros/sucessos -->
          <div id="feedback-message" class="messages" style="margin-top:1rem;"></div>
        </main>

        <%@ include file="includes/footer.jsp" %>
          <script src="${pageContext.request.contextPath}/js/cart.js" defer></script>
    </body>

    </html>