document.addEventListener("DOMContentLoaded", () => {
  const cartItemsContainer = document.getElementById("cart-items-container");
  const finalizeOrderBtn = document.getElementById("finalize-order-btn");
  const couponInput = document.getElementById("coupon-code");
  const applyCouponBtn = document.getElementById("apply-coupon-btn");
  const feedbackDiv = document.getElementById("feedback-message");
  const couponFeedbackDiv = document.getElementById("coupon-feedback");
  const resumoContainer = document.getElementById("cart-summary-values");

  const token = localStorage.getItem("jwtToken");
  if (!token) {
    window.location.href = "/login";
    return;
  }

  let cart = JSON.parse(localStorage.getItem("shoppingCart")) || [];
  let appliedCoupon = null;
  let cupomPercentualDesconto = 0; // para depois poder estender

  function renderCartItems() {
    cartItemsContainer.innerHTML = "";

    if (cart.length === 0) {
      cartItemsContainer.innerHTML = `<p class="empty-list-message">Seu carrinho está vazio.</p>`;
      finalizeOrderBtn.disabled = true;
      resumoContainer.innerHTML = "";
      return;
    }

    const table = document.createElement("table");
    table.className = "product-table";

    table.innerHTML = `
      <thead>
        <tr>
          <th>Produto</th>
          <th>Valor Cheio (R$)</th>
          <th>Desconto Fornecedor (R$)</th>
          <th>Quantidade</th>
          <th>Valor Total (R$)</th>
        </tr>
      </thead>
    `;

    const tbody = document.createElement("tbody");

    cart.forEach((item) => {
      const valorCheio = item.preco || 0;
      const descontoFornecedor =
        valorCheio * ((item.percentualDesconto || 0) / 100);
      const valorComDesconto = valorCheio - descontoFornecedor;
      const valorTotalItem = valorComDesconto * item.quantidade;

      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${item.nome}</td>
        <td>${valorCheio.toFixed(2).replace(".", ",")}</td>
        <td>- ${descontoFornecedor.toFixed(2).replace(".", ",")}</td>
        <td>${item.quantidade}</td>
        <td>${valorTotalItem.toFixed(2).replace(".", ",")}</td>
      `;

      tbody.appendChild(tr);
    });

    table.appendChild(tbody);
    cartItemsContainer.appendChild(table);

    finalizeOrderBtn.disabled = false;

    renderResumoPedido();
  }

  function calcularResumoCarrinho() {
    let valorBruto = 0;
    let descontoFornecedor = 0;

    cart.forEach((item) => {
      valorBruto += (item.preco || 0) * item.quantidade;
      descontoFornecedor +=
        (item.preco || 0) *
        ((item.percentualDesconto || 0) / 100) *
        item.quantidade;
    });

    const subtotalComDescontoFornecedor = valorBruto - descontoFornecedor;

    // cupomPercentualDesconto deve ser ajustado ao aplicar cupom (atualmente só simulação)
    let descontoCupom = appliedCoupon
      ? subtotalComDescontoFornecedor * (cupomPercentualDesconto / 100)
      : 0;

    if (descontoCupom > subtotalComDescontoFornecedor) {
      descontoCupom = subtotalComDescontoFornecedor;
    }

    let valorFinal = subtotalComDescontoFornecedor - descontoCupom;
    if (valorFinal < 0) valorFinal = 0;

    return {
      valorBruto,
      descontoFornecedor,
      descontoCupom,
      valorFinal,
    };
  }

  function renderResumoPedido() {
    if (!resumoContainer) return;

    if (cart.length === 0) {
      resumoContainer.innerHTML = "";
      return;
    }

    const resumo = calcularResumoCarrinho();

    resumoContainer.innerHTML = `
      <p><strong>Valor Bruto:</strong> R$ ${resumo.valorBruto
        .toFixed(2)
        .replace(".", ",")}</p>
      <p><strong>Desconto do Fornecedor:</strong> - R$ ${resumo.descontoFornecedor
        .toFixed(2)
        .replace(".", ",")}</p>
      ${
        appliedCoupon
          ? `<p><strong>Desconto do Cupom (${appliedCoupon}):</strong> - R$ ${resumo.descontoCupom
              .toFixed(2)
              .replace(".", ",")}</p>`
          : ""
      }
      <p><strong>Valor Final:</strong> R$ ${resumo.valorFinal
        .toFixed(2)
        .replace(".", ",")}</p>
    `;
  }

  async function validarCupom(codigo) {
    try {
      const response = await fetch(`/api/v1/cupons/${codigo}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Cupom inválido");
      }
      // Exemplo para simular cupom percentual, pode adaptar para tipo real
      // Aqui só um exemplo, se seu backend retorna tipo/valor, armazene para usar no cálculo
      const cupomData = await response.json();
      cupomPercentualDesconto =
        cupomData.tipoDesconto === "P" ? parseFloat(cupomData.valor) : 0;
      return cupomData;
    } catch (error) {
      throw error;
    }
  }

  applyCouponBtn.addEventListener("click", async () => {
    const code = couponInput.value.trim();
    couponFeedbackDiv.textContent = "";
    couponFeedbackDiv.className = "";

    if (!code) {
      couponFeedbackDiv.textContent = "Informe um código de cupom válido.";
      couponFeedbackDiv.classList.add("error");
      appliedCoupon = null;
      cupomPercentualDesconto = 0;
      renderResumoPedido();
      return;
    }

    applyCouponBtn.disabled = true;
    couponInput.disabled = true;

    try {
      const cupom = await validarCupom(code);
      appliedCoupon = code;
      couponFeedbackDiv.textContent = `Cupom "${code}" aplicado com sucesso!`;
      couponFeedbackDiv.classList.add("success");
      renderResumoPedido();
    } catch (error) {
      appliedCoupon = null;
      cupomPercentualDesconto = 0;
      couponFeedbackDiv.textContent = error.message;
      couponFeedbackDiv.classList.add("error");
      renderResumoPedido();
    } finally {
      applyCouponBtn.disabled = false;
      couponInput.disabled = false;
    }
  });

  async function finalizarPedido() {
    if (cart.length === 0) {
      mostrarMensagem("O carrinho está vazio.", "error");
      return;
    }

    const pedidoDTO = {
      itens: cart.map((item) => ({
        produtoId: item.id,
        quantidade: item.quantidade,
      })),
      codigoCupom: appliedCoupon || null,
    };

    finalizeOrderBtn.disabled = true;

    try {
      const response = await fetch("/api/v1/pedidos", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(pedidoDTO),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Erro ao finalizar pedido");
      }

      await response.json();
      mostrarMensagem("Pedido realizado com sucesso!", "success");
      localStorage.removeItem("shoppingCart");
      cart = [];
      appliedCoupon = null;
      cupomPercentualDesconto = 0;
      couponInput.value = "";
      couponFeedbackDiv.textContent = "";
      couponFeedbackDiv.className = "";
      renderCartItems();
    } catch (error) {
      mostrarMensagem(error.message, "error");
    } finally {
      finalizeOrderBtn.disabled = false;
    }
  }

  function mostrarMensagem(mensagem, tipo) {
    feedbackDiv.textContent = mensagem;
    feedbackDiv.className =
      tipo === "success" ? "success-message" : "error-message";
    feedbackDiv.style.display = "block";
    setTimeout(() => {
      feedbackDiv.style.display = "none";
    }, 5000);
  }

  finalizeOrderBtn.addEventListener("click", finalizarPedido);

  renderCartItems();
});
