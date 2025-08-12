document.addEventListener("DOMContentLoaded", () => {
  const token = localStorage.getItem("jwtToken");
  if (!token) {
    window.location.href = "/login";
    return;
  }

  // Elementos DOM para as abas
  const tabs = document.querySelectorAll(".tab-button");
  const contents = document.querySelectorAll(".tab-content");

  // Elementos DOM para a aba de produtos
  const productGrid = document.getElementById("product-grid");
  const paginacaoProdutos = document.getElementById("paginacao-produtos");
  const TAB_PRODUTOS_ID = "produtos";

  // Elementos DOM para a aba de pedidos
  const pedidosTbody = document.getElementById("pedidos-tbody");
  const paginacaoPedidos = document.getElementById("paginacao-pedidos");
  const TAB_PEDIDOS_ID = "pedidos";

  // Elementos DOM da modal de detalhes do pedido
  const modalDetalhesPedido = document.getElementById("modal-detalhes-pedido");
  const modalDetalhesBody = document.getElementById("modal-detalhes-body");
  const modalCloseBtns = document.querySelectorAll(".modal-close-btn");

  // Estado da paginação
  let paginaAtualProdutos = 0;
  const tamanhoPagina = 8;
  let paginaAtualPedidos = 0;

  // --- LÓGICA DAS ABAS ---
  function setActiveTab(tabId) {
    tabs.forEach((tab) => {
      if (tab.dataset.tab === tabId) {
        tab.classList.add("active");
      } else {
        tab.classList.remove("active");
      }
    });
    contents.forEach((content) => {
      if (content.id === tabId) {
        content.classList.add("active");
      } else {
        content.classList.remove("active");
      }
    });
  }

  tabs.forEach((tab) => {
    tab.addEventListener("click", () => {
      const tabId = tab.dataset.tab;
      setActiveTab(tabId);
      if (tabId === TAB_PRODUTOS_ID) {
        carregarProdutos(paginaAtualProdutos);
      } else if (tabId === TAB_PEDIDOS_ID) {
        carregarPedidos(paginaAtualPedidos);
      }
    });
  });

  // --- LÓGICA DA MODAL ---
  function openModal() {
    modalDetalhesPedido.classList.add("active");
  }

  function closeModal() {
    modalDetalhesPedido.classList.remove("active");
    modalDetalhesBody.innerHTML = "";
  }

  modalCloseBtns.forEach((btn) => btn.addEventListener("click", closeModal));

  modalDetalhesPedido.addEventListener("click", (e) => {
    if (e.target === modalDetalhesPedido) {
      closeModal();
    }
  });

  document.addEventListener("keydown", (e) => {
    if (
      e.key === "Escape" &&
      modalDetalhesPedido.classList.contains("active")
    ) {
      closeModal();
    }
  });

  // --- LÓGICA DE PRODUTOS (SEM ALTERAÇÕES) ---
  async function carregarProdutos(pagina = 0) {
    paginaAtualProdutos = pagina;
    productGrid.innerHTML = `<div class="spinner-wrapper"><div class="spinner"></div></div>`;

    try {
      const res = await fetch(
        `/api/v1/produtos/meus-produtos?page=${pagina}&size=${tamanhoPagina}`,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      if (!res.ok) throw new Error("Erro ao carregar seus produtos.");

      const data = await res.json();
      renderizarProdutos(data.content);
      renderizarPaginacao(
        data.totalPages,
        paginaAtualProdutos,
        paginacaoProdutos,
        carregarProdutos
      );
    } catch (error) {
      productGrid.innerHTML = `<p class="empty-message">Erro ao carregar produtos. Tente novamente.</p>`;
      paginacaoProdutos.innerHTML = "";
      console.error(error);
    }
  }

  function renderizarProdutos(produtos) {
    productGrid.innerHTML = "";
    if (!produtos || produtos.length === 0) {
      productGrid.innerHTML = `<p class="empty-message">Nenhum produto encontrado. Adicione seu primeiro produto!</p>`;
      return;
    }

    produtos.forEach((produto) => {
      const card = document.createElement("article");
      card.className = "product-card";
      card.setAttribute("role", "listitem");

      const precoFormatado = produto.preco.toFixed(2).replace(".", ",");
      const statusText = produto.ativo ? "Ativo" : "Inativo";

      const categoriasTags = document.createElement("div");
      categoriasTags.className = "category-list";
      if (produto.categorias && produto.categorias.length > 0) {
        produto.categorias.forEach((c) => {
          const tag = document.createElement("span");
          tag.className = "category-tag";
          tag.textContent = c.nome;
          categoriasTags.appendChild(tag);
        });
      }

      const descricao = produto.descricao || "Sem descrição.";

      card.innerHTML = `
                <h3>${produto.nome}</h3>
                <p class="description">${descricao}</p>
                ${categoriasTags.outerHTML}
                <p class="price">R$ ${precoFormatado}</p>
                <div class="action-buttons">
                    <a href="/fornecedor/produtos/editar/${
                      produto.id
                    }" class="btn edit-btn">Editar</a>
                    <button class="btn toggle-status-btn ${
                      produto.ativo ? "" : "reativar"
                    }" data-id="${produto.id}" data-ativo="${produto.ativo}">
                        ${produto.ativo ? "Desativar" : "Reativar"}
                    </button>
                </div>
            `;

      productGrid.appendChild(card);

      card
        .querySelector(".toggle-status-btn")
        .addEventListener("click", (e) => {
          const btn = e.target;
          const produtoId = btn.dataset.id;
          const isAtivo = btn.dataset.ativo === "true";
          if (isAtivo) {
            desativarProduto(produtoId);
          } else {
            reativarProduto(produtoId);
          }
        });
    });
  }

  async function desativarProduto(id) {
    try {
      const res = await fetch(`/api/v1/produtos/${id}`, {
        method: "DELETE",
        headers: { Authorization: `Bearer ${token}` },
      });
      if (!res.ok) throw new Error("Erro ao desativar produto.");

      carregarProdutos(paginaAtualProdutos);
      mostrarToast("Produto desativado com sucesso!", "success");
    } catch (error) {
      mostrarToast(error.message, "error");
      console.error(error);
    }
  }

  async function reativarProduto(id) {
    try {
      const res = await fetch(`/api/v1/produtos/${id}/reativar`, {
        method: "POST",
        headers: { Authorization: `Bearer ${token}` },
      });
      if (!res.ok) throw new Error("Erro ao reativar produto.");

      carregarProdutos(paginaAtualProdutos);
      mostrarToast("Produto reativado com sucesso!", "success");
    } catch (error) {
      mostrarToast(error.message, "error");
      console.error(error);
    }
  }

  // --- LÓGICA DE PEDIDOS (COM MODAL DE DETALHES) ---
  async function carregarPedidos(pagina = 0) {
    paginaAtualPedidos = pagina;
    pedidosTbody.innerHTML = `<tr><td colspan="5"><div class="spinner-wrapper"><div class="spinner"></div></div></td></tr>`;

    try {
      const res = await fetch(
        `/api/v1/pedidos/fornecedor?page=${pagina}&size=${tamanhoPagina}`,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      if (!res.ok) throw new Error("Erro ao carregar pedidos.");

      const data = await res.json();
      renderizarPedidos(data.content);
      renderizarPaginacao(
        data.totalPages,
        paginaAtualPedidos,
        paginacaoPedidos,
        carregarPedidos
      );
    } catch (error) {
      pedidosTbody.innerHTML = `<tr><td colspan="5" class="empty-message">Erro ao carregar pedidos. Tente novamente.</td></tr>`;
      paginacaoPedidos.innerHTML = "";
      console.error(error);
    }
  }

  function renderizarPedidos(pedidos) {
    pedidosTbody.innerHTML = "";
    if (!pedidos || pedidos.length === 0) {
      pedidosTbody.innerHTML = `<tr><td colspan="5" class="empty-message">Nenhum pedido encontrado.</td></tr>`;
      return;
    }

    pedidos.forEach((pedido) => {
      const tr = document.createElement("tr");
      const dataPedido = new Date(pedido.dataPedido).toLocaleDateString(
        "pt-BR"
      );

      const statusClass = `status-${pedido.status.toLowerCase()}`;

      tr.innerHTML = `
                <td>#${pedido.id.substring(0, 8)}...</td>
                <td>${pedido.nomeCliente}</td>
                <td>${dataPedido}</td>
                <td><span class="${statusClass}">${pedido.status}</span></td>
                <td>
                    <div class="action-buttons">
                        <button class="btn primary-btn btn-detalhes-pedido" data-id="${
                          pedido.id
                        }">Detalhes</button>
                    </div>
                </td>
            `;
      pedidosTbody.appendChild(tr);

      // Adiciona o eventListener para o botão de detalhes
      tr.querySelector(".btn-detalhes-pedido").addEventListener(
        "click",
        (e) => {
          const pedidoId = e.target.dataset.id;
          carregarDetalhesPedido(pedidoId);
        }
      );
    });
  }

  async function carregarDetalhesPedido(pedidoId) {
    openModal();
    modalDetalhesBody.innerHTML = `<div class="spinner-wrapper"><div class="spinner"></div></div>`;

    try {
      const res = await fetch(`/api/v1/pedidos/fornecedor/${pedidoId}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (!res.ok) throw new Error("Detalhes do pedido não encontrados.");

      const pedido = await res.json();
      renderizarDetalhesPedido(pedido);
    } catch (error) {
      modalDetalhesBody.innerHTML = `<p class="error-message">Erro: ${error.message}</p>`;
    }
  }

  function renderizarDetalhesPedido(pedido) {
    const dataPedido = new Date(pedido.dataPedido).toLocaleString("pt-BR");
    const statusClass = `status-${pedido.status.toLowerCase()}`;

    // Calcula o valor total do pedido
    const itensHtml = pedido.itens
      .map((item) => {
        const totalItem = item.quantidade * item.precoUnitarioCobrado;
        return `
            <li class="item-pedido">
                <span class="item-nome">${item.nomeProduto}</span>
                <span class="item-quantidade">Qtd: ${item.quantidade}</span>
                <span class="item-preco">R$ ${item.precoUnitarioCobrado
                  .toFixed(2)
                  .replace(".", ",")}</span>
            </li>
        `;
      })
      .join("");

    // Adiciona o valor total ao cabeçalho da modal
    modalDetalhesBody.innerHTML = `
        <div class="detalhes-header">
            <p><strong>Cliente:</strong> ${pedido.nomeCliente}</p>
            <p><strong>Data:</strong> ${dataPedido}</p>
            <p><strong>Status:</strong> <span class="${statusClass}">${pedido.status}</span></p>
            <hr/>
            <h4>Itens do Pedido:</h4>
        </div>
        <div class="detalhes-body">
            <ul class="lista-itens">${itensHtml}</ul>
        </div>
    `;
  }

  // LÓGICA GENÉRICA DE PAGINAÇÃO
  function renderizarPaginacao(
    totalPages,
    currentPage,
    container,
    onPageClick
  ) {
    container.innerHTML = "";
    if (totalPages <= 1) return;

    const btnPrev = document.createElement("button");
    btnPrev.textContent = "«";
    btnPrev.disabled = currentPage === 0;
    btnPrev.addEventListener("click", () => onPageClick(currentPage - 1));
    container.appendChild(btnPrev);

    for (let i = 0; i < totalPages; i++) {
      const btn = document.createElement("button");
      btn.textContent = i + 1;
      btn.disabled = i === currentPage;
      btn.addEventListener("click", () => onPageClick(i));
      container.appendChild(btn);
    }

    const btnNext = document.createElement("button");
    btnNext.textContent = "»";
    btnNext.disabled = currentPage === totalPages - 1;
    btnNext.addEventListener("click", () => onPageClick(currentPage + 1));
    container.appendChild(btnNext);
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

  carregarProdutos(0);
});
