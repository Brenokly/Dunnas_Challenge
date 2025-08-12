document.addEventListener("DOMContentLoaded", () => {
  const token = localStorage.getItem("jwtToken");
  if (!token) {
    window.location.href = "/login";
    return;
  }

  // Abas
  const tabs = document.querySelectorAll(".tab-button");
  const contents = document.querySelectorAll(".tab-content");

  tabs.forEach((tab) => {
    tab.addEventListener("click", () => {
      tabs.forEach((t) => t.classList.remove("active"));
      contents.forEach((c) => c.classList.remove("active"));

      tab.classList.add("active");
      document.getElementById(tab.dataset.tab).classList.add("active");
    });
  });

  // Paginação pedidos
  let paginaPedidos = 0;
  const tamanhoPagina = 5;

  async function carregarPedidos(pagina = 0) {
    const url = `/api/v1/pedidos?page=${pagina}&size=${tamanhoPagina}&sort=dataPedido,desc`;
    const res = await fetch(url, {
      headers: { Authorization: `Bearer ${token}` },
    });
    if (!res.ok) {
      alert("Erro ao carregar pedidos");
      return;
    }
    const data = await res.json();
    renderizarPedidos(data.content);
    renderizarPaginacao(
      data.totalPages,
      pagina,
      "pedidos-paginacao",
      carregarPedidos
    );
  }

  function renderizarPedidos(pedidos) {
    const tbody = document.querySelector("#pedidos-table tbody");
    tbody.innerHTML = "";

    if (pedidos.length === 0) {
      tbody.innerHTML = `<tr><td colspan="4">Nenhum pedido encontrado.</td></tr>`;
      return;
    }

    pedidos.forEach((pedido) => {
      const dataPedido = new Date(pedido.dataPedido).toLocaleDateString(
        "pt-BR"
      );
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td>${pedido.id}</td>
        <td>${dataPedido}</td>
        <td>${pedido.status}</td>
        <td>R$ ${pedido.valorFinal.toFixed(2).replace(".", ",")}</td>
      `;
      tbody.appendChild(tr);
    });
  }

  // Paginação transações
  let paginaTransacoes = 0;

  async function carregarTransacoes(pagina = 0) {
    const url = `/api/v1/transacoes/meu-historico?page=${pagina}&size=${tamanhoPagina}&sort=dataTransacao,desc`;
    const res = await fetch(url, {
      headers: { Authorization: `Bearer ${token}` },
    });
    if (!res.ok) {
      alert("Erro ao carregar transações");
      return;
    }
    const data = await res.json();
    renderizarTransacoes(data.content);
    renderizarPaginacao(
      data.totalPages,
      pagina,
      "transacoes-paginacao",
      carregarTransacoes
    );
  }

  function renderizarTransacoes(transacoes) {
    const tbody = document.querySelector("#transacoes-table tbody");
    tbody.innerHTML = "";

    if (transacoes.length === 0) {
      tbody.innerHTML = `<tr><td colspan="5">Nenhuma transação encontrada.</td></tr>`;
      return;
    }

    transacoes.forEach((tx) => {
      const dataTx = new Date(tx.dataTransacao).toLocaleDateString("pt-BR");
      const valorStr =
        (tx.valor >= 0 ? "R$ " : "-R$ ") +
        Math.abs(tx.valor).toFixed(2).replace(".", ",");

      const tr = document.createElement("tr");
      tr.innerHTML = `
    <td>${tx.id}</td>
    <td>${dataTx}</td>
    <td>${tx.tipoTransacao}</td>
    <td>${valorStr}</td>
  `;
      tbody.appendChild(tr);
    });
  }

  // Renderiza paginação simples
  function renderizarPaginacao(
    totalPages,
    paginaAtual,
    containerId,
    onPageClick
  ) {
    const container = document.getElementById(containerId);
    container.innerHTML = "";

    if (totalPages <= 1) return;

    for (let i = 0; i < totalPages; i++) {
      const btn = document.createElement("button");
      btn.textContent = i + 1;
      if (i === paginaAtual) btn.disabled = true;

      btn.addEventListener("click", () => {
        onPageClick(i);
      });

      container.appendChild(btn);
    }
  }

  // Inicializa carregamento da aba ativa (pedidos)
  carregarPedidos(paginaPedidos);
  carregarTransacoes(paginaTransacoes);
});
