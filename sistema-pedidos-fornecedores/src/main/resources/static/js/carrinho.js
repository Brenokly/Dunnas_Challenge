document.addEventListener("DOMContentLoaded", () => {
  const cartItemsContainer = document.getElementById("cart-items-container");
  const finalizeOrderBtn = document.getElementById("finalize-order-btn");
  const resumoContainer = document.getElementById("cart-summary-values");

  let cart = JSON.parse(localStorage.getItem("shoppingCart")) || [];
  let appliedCoupons = {};
  let fornecedoresCache = {};

  function agruparItensPorFornecedor(cart) {
    return cart.reduce((acc, item) => {
      if (!acc[item.fornecedorId]) {
        acc[item.fornecedorId] = [];
      }
      acc[item.fornecedorId].push(item);
      return acc;
    }, {});
  }

  async function buscarNomeFornecedor(fornecedorId) {
    if (fornecedoresCache[fornecedorId]) {
      return fornecedoresCache[fornecedorId];
    }
    const res = await fetch(`/api/v1/fornecedores/${fornecedorId}`);
    if (!res.ok) {
      fornecedoresCache[fornecedorId] = `Fornecedor ${fornecedorId}`;
      return fornecedoresCache[fornecedorId];
    }
    const data = await res.json();
    fornecedoresCache[fornecedorId] = data.nome || `Fornecedor ${fornecedorId}`;
    return fornecedoresCache[fornecedorId];
  }

  async function validarCupom(fornecedorId, codigo) {
    const res = await fetch(`/api/v1/cupons/${fornecedorId}/cupom/${codigo}`);
    if (!res.ok) {
      const errorData = await res.json();
      throw new Error(
        errorData.message || "Cupom inválido para este fornecedor"
      );
    }
    return await res.json();
  }

  function atualizarQuantidadeItem(produtoId, fornecedorId, acao) {
    const itemIndex = cart.findIndex(
      (item) => item.id === produtoId && item.fornecedorId === fornecedorId
    );
    if (itemIndex > -1) {
      if (acao === "incrementar") {
        cart[itemIndex].quantidade++;
      } else if (acao === "decrementar") {
        if (cart[itemIndex].quantidade > 1) {
          cart[itemIndex].quantidade--;
        } else {
          cart.splice(itemIndex, 1);
          mostrarToast("Item removido do carrinho.", "success");
        }
      }
      localStorage.setItem("shoppingCart", JSON.stringify(cart));
      renderCartItems();
    }
  }

  async function renderCartItems() {
    cartItemsContainer.innerHTML = "";

    if (cart.length === 0) {
      cartItemsContainer.innerHTML = `<p class="empty-list-message">Seu carrinho está vazio.</p>`;
      finalizeOrderBtn.disabled = true;
      resumoContainer.innerHTML = "";
      // Garantir que o contador do header seja atualizado
      if (typeof updateCartCounter === "function") {
        updateCartCounter();
      }
      return;
    }

    const grupos = agruparItensPorFornecedor(cart);
    const fornecedorIds = Object.keys(grupos);
    const nomesPromises = fornecedorIds.map((fornecedorId) =>
      buscarNomeFornecedor(fornecedorId)
    );
    const nomesFornecedoresArray = await Promise.all(nomesPromises);

    const nomesPorId = fornecedorIds.reduce((acc, id, index) => {
      acc[id] = nomesFornecedoresArray[index];
      return acc;
    }, {});

    for (const fornecedorId in grupos) {
      const itens = grupos[fornecedorId];
      if (!itens.length) continue;

      const nomeFornecedor = nomesPorId[fornecedorId];

      const fornecedorDiv = document.createElement("div");
      fornecedorDiv.className = "fornecedor-group";

      const fornecedorTitulo = document.createElement("h3");
      fornecedorTitulo.textContent = nomeFornecedor;
      fornecedorDiv.appendChild(fornecedorTitulo);

      const table = document.createElement("table");
      table.className = "product-table";
      table.innerHTML = `
        <thead>
          <tr>
            <th>Produto</th>
            <th>Valor Cheio (R$)</th>
            <th>Desconto Fornecedor (R$)</th>
            <th>Valor Total (R$)</th>
            <th class="text-center">Ações</th> </tr>
        </thead>
      `;
      const tbody = document.createElement("tbody");

      itens.forEach((item) => {
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
          <td>${valorTotalItem.toFixed(2).replace(".", ",")}</td>
          <td class="text-center">
            <div class="quantidade-controls" data-produto-id="${
              item.id
            }" data-fornecedor-id="${item.fornecedorId}">
              <button class="decrementar-btn" aria-label="Remover um item">-</button>
              <span class="quantidade-display">${item.quantidade}</span>
              <button class="incrementar-btn" aria-label="Adicionar um item">+</button>
            </div>
          </td>
        `;
        tbody.appendChild(tr);
      });

      table.appendChild(tbody);
      fornecedorDiv.appendChild(table);

      const cupomDiv = document.createElement("div");
      cupomDiv.className = "coupon-section";
      cupomDiv.innerHTML = `
        <input type="text" class="coupon-input" placeholder="Código do cupom para este fornecedor" id="coupon-code-${fornecedorId}" />
        <button class="coupon-btn" id="apply-coupon-btn-${fornecedorId}">Aplicar Cupom</button>
      `;

      fornecedorDiv.appendChild(cupomDiv);
      cartItemsContainer.appendChild(fornecedorDiv);

      if (appliedCoupons[fornecedorId]) {
        const input = cupomDiv.querySelector(`#coupon-code-${fornecedorId}`);
        input.value = appliedCoupons[fornecedorId].codigo;
      }

      const applyBtn = cupomDiv.querySelector(
        `#apply-coupon-btn-${fornecedorId}`
      );
      applyBtn.addEventListener("click", async () => {
        const input = cupomDiv.querySelector(`#coupon-code-${fornecedorId}`);
        const codigo = input.value.trim();
        if (!codigo) {
          mostrarToast("Informe um código de cupom válido.", "error");
          delete appliedCoupons[fornecedorId];
          renderResumoPedido();
          return;
        }
        applyBtn.disabled = true;
        input.disabled = true;
        try {
          const cupom = await validarCupom(fornecedorId, codigo);
          if (!cupom.ativo) throw new Error("Cupom inativo.");
          const hoje = new Date().toISOString().slice(0, 10);
          if (cupom.dataValidade < hoje) throw new Error("Cupom expirado.");
          if (
            cupom.limiteDeUsos !== null &&
            cupom.usosAtuais >= cupom.limiteDeUsos
          )
            throw new Error("Cupom já atingiu o limite de usos.");
          const subtotalFornecedor = calcularSubtotalFornecedor(itens);
          if (
            cupom.valorMinimoPedido !== null &&
            subtotalFornecedor < cupom.valorMinimoPedido
          ) {
            throw new Error(
              `Pedido mínimo de R$ ${cupom.valorMinimoPedido
                .toFixed(2)
                .replace(".", ",")} não atingido para este cupom.`
            );
          }
          appliedCoupons[fornecedorId] = cupom;
          mostrarToast(
            `Cupom "${codigo}" aplicado com sucesso! Desconto: ${
              cupom.tipoDesconto === "P"
                ? cupom.valor + "%"
                : "R$ " + cupom.valor.toFixed(2).replace(".", ",")
            }`,
            "success"
          );
          renderResumoPedido();
        } catch (error) {
          delete appliedCoupons[fornecedorId];
          mostrarToast(error.message, "error");
          renderResumoPedido();
        } finally {
          applyBtn.disabled = false;
          input.disabled = false;
        }
      });
    }

    document.querySelectorAll(".incrementar-btn").forEach((button) => {
      button.addEventListener("click", (e) => {
        const controls = e.target.closest(".quantidade-controls");
        const produtoId = controls.dataset.produtoId;
        const fornecedorId = controls.dataset.fornecedorId;
        atualizarQuantidadeItem(produtoId, fornecedorId, "incrementar");
      });
    });

    document.querySelectorAll(".decrementar-btn").forEach((button) => {
      button.addEventListener("click", (e) => {
        const controls = e.target.closest(".quantidade-controls");
        const produtoId = controls.dataset.produtoId;
        const fornecedorId = controls.dataset.fornecedorId;
        atualizarQuantidadeItem(produtoId, fornecedorId, "decrementar");
      });
    });

    if (typeof updateCartCounter === "function") {
      updateCartCounter();
    }

    finalizeOrderBtn.disabled = false;
    renderResumoPedido();
  }

  function calcularSubtotalFornecedor(itens) {
    let valorBruto = 0;
    let descontoFornecedor = 0;
    itens.forEach((item) => {
      valorBruto += (item.preco || 0) * item.quantidade;
      descontoFornecedor +=
        (item.preco || 0) *
        ((item.percentualDesconto || 0) / 100) *
        item.quantidade;
    });
    return valorBruto - descontoFornecedor;
  }

  function calcularResumoPedido() {
    let valorBrutoTotal = 0;
    let descontoFornecedorTotal = 0;
    let descontoCupomTotal = 0;

    const grupos = agruparItensPorFornecedor(cart);

    for (const fornecedorId in grupos) {
      const itens = grupos[fornecedorId];

      let valorBruto = 0;
      let descontoFornecedor = 0;

      itens.forEach((item) => {
        valorBruto += (item.preco || 0) * item.quantidade;
        descontoFornecedor +=
          (item.preco || 0) *
          ((item.percentualDesconto || 0) / 100) *
          item.quantidade;
      });

      const subtotalComDescontoFornecedor = valorBruto - descontoFornecedor;
      valorBrutoTotal += valorBruto;
      descontoFornecedorTotal += descontoFornecedor;

      const cupom = appliedCoupons[fornecedorId];
      if (cupom) {
        let descontoCupom = 0;
        if (cupom.tipoDesconto === "P") {
          descontoCupom =
            subtotalComDescontoFornecedor * (parseFloat(cupom.valor) / 100);
        } else if (cupom.tipoDesconto === "F") {
          descontoCupom = parseFloat(cupom.valor);
        }
        if (descontoCupom > subtotalComDescontoFornecedor) {
          descontoCupom = subtotalComDescontoFornecedor;
        }
        descontoCupomTotal += descontoCupom;
      }
    }

    let valorFinal =
      valorBrutoTotal - descontoFornecedorTotal - descontoCupomTotal;
    if (valorFinal < 0) valorFinal = 0;

    return {
      valorBrutoTotal,
      descontoFornecedorTotal,
      descontoCupomTotal,
      valorFinal,
    };
  }

  function renderResumoPedido() {
    if (!resumoContainer) return;
    if (cart.length === 0) {
      resumoContainer.innerHTML = "";
      return;
    }
    const resumo = calcularResumoPedido();
    resumoContainer.innerHTML = `
      <p><strong>Valor Bruto:</strong> R$ ${resumo.valorBrutoTotal
        .toFixed(2)
        .replace(".", ",")}</p>
      <p><strong>Desconto do Fornecedor:</strong> - R$ ${resumo.descontoFornecedorTotal
        .toFixed(2)
        .replace(".", ",")}</p>
      <p><strong>Desconto dos Cupons:</strong> - R$ ${resumo.descontoCupomTotal
        .toFixed(2)
        .replace(".", ",")}</p>
      <p><strong>Valor Final:</strong> R$ ${resumo.valorFinal
        .toFixed(2)
        .replace(".", ",")}</p>
    `;
  }

  function mostrarToast(mensagem, tipo) {
    let toast = document.getElementById("toast-message");
    if (!toast) {
      toast = document.createElement("div");
      toast.id = "toast-message";
      toast.className = "toast";
      document.body.appendChild(toast);
    }
    toast.textContent = mensagem;
    toast.className = `toast show ${tipo}`;
    setTimeout(() => {
      toast.className = "toast";
    }, 4000);
  }

  async function finalizarPedido() {
    if (cart.length === 0) {
      mostrarToast("O carrinho está vazio.", "error");
      return;
    }
    const grupos = agruparItensPorFornecedor(cart);
    finalizeOrderBtn.disabled = true;
    try {
      for (const fornecedorId in grupos) {
        const itens = grupos[fornecedorId];
        let itensPedido = itens.map((item) => ({
          produtoId: item.id,
          quantidade: item.quantidade,
        }));
        const cupomFornecedor = appliedCoupons[fornecedorId];
        const codigoCupom = cupomFornecedor ? cupomFornecedor.codigo : null;
        const pedidoDTO = {
          itens: itensPedido,
          codigoCupom: codigoCupom,
        };
        const response = await fetch("/api/v1/pedidos", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(pedidoDTO),
        });
        if (!response.ok) {
          const errorData = await response.json();
          throw new Error(
            `Erro ao finalizar pedido do fornecedor ${
              fornecedoresCache[fornecedorId]
            }: ${errorData || "Erro desconhecido"}`
          );
        }
        await response.json();
      }
      mostrarToast("Todos os pedidos foram realizados com sucesso!", "success");
      localStorage.removeItem("shoppingCart");
      cart = [];
      appliedCoupons = {};
      renderCartItems();
    } catch (error) {
      mostrarToast(error.message, "error");
    } finally {
      finalizeOrderBtn.disabled = false;
    }
  }

  finalizeOrderBtn.addEventListener("click", finalizarPedido);

  renderCartItems();
});
