document.addEventListener("DOMContentLoaded", () => {
  const cuponsTbody = document.getElementById("cupons-tbody");
  const paginacaoCupons = document.getElementById("paginacao-cupons");
  let paginaAtualCupons = 0;
  const tamanhoPagina = 10;

  async function carregarCupons(pagina = 0) {
    paginaAtualCupons = pagina;
    cuponsTbody.innerHTML = `<tr><td colspan="7"><div class="spinner-wrapper"><div class="spinner"></div></div></td></tr>`;

    try {
      const url = `/api/v1/cupons?page=${pagina}&size=${tamanhoPagina}&sort=dataValidade,desc`;

      const res = await fetch(url);
      if (!res.ok) throw new Error("Erro ao carregar cupons.");

      const data = await res.json();
      renderizarCupons(data.content);
      renderizarPaginacao(
        data.totalPages,
        paginaAtualCupons,
        paginacaoCupons,
        carregarCupons
      );
    } catch (error) {
      cuponsTbody.innerHTML = `<tr><td colspan="7" class="empty-message">Erro ao carregar cupons. Tente novamente.</td></tr>`;
      paginacaoCupons.innerHTML = "";
      console.error(error);
    }
  }

  function renderizarCupons(cupons) {
    cuponsTbody.innerHTML = "";
    if (!cupons || cupons.length === 0) {
      cuponsTbody.innerHTML = `<tr><td colspan="7" class="empty-message">Nenhum cupom cadastrado.</td></tr>`;
      return;
    }

    cupons.forEach((cupom) => {
      const tr = document.createElement("tr");
      const dataValidadeFormatada = new Date(
        cupom.dataValidade
      ).toLocaleDateString("pt-BR");
      const statusClass = cupom.ativo ? "status-ativo" : "status-inativo";
      const statusText = cupom.ativo ? "Ativo" : "Inativo";
      const valorFormatado =
        cupom.tipoDesconto === "P"
          ? `${cupom.valor}%`
          : `R$ ${cupom.valor.toFixed(2).replace(".", ",")}`;

      tr.innerHTML = `
                <td>${cupom.codigo}</td>
                <td>${valorFormatado}</td>
                <td>${cupom.tipoDesconto === "P" ? "Percentual" : "Fixo"}</td>
                <td>${dataValidadeFormatada}</td>
                <td>${cupom.usosAtuais || 0} de ${
        cupom.limiteDeUsos || "ilimitado"
      }</td>
                <td><span class="${statusClass}">${statusText}</span></td>
                <td>
                    <div class="action-buttons">
                        <button class="btn toggle-status-btn ${
                          cupom.ativo ? "" : "reativar"
                        }" data-id="${cupom.id}" data-ativo="${cupom.ativo}">
                            ${cupom.ativo ? "Desativar" : "Reativar"}
                        </button>
                    </div>
                </td>
            `;
      cuponsTbody.appendChild(tr);

      tr.querySelector(".toggle-status-btn").addEventListener("click", (e) => {
        const btn = e.target;
        const cupomId = btn.dataset.id;
        const isAtivo = btn.dataset.ativo === "true";
        if (isAtivo) {
          desativarCupom(cupomId);
        } else {
          reativarCupom(cupomId);
        }
      });
    });
  }

  async function desativarCupom(id) {
    try {
      const res = await fetch(`/api/v1/cupons/${id}`, {
        method: "DELETE"
      });
      if (!res.ok) throw new Error("Erro ao desativar cupom.");

      carregarCupons(paginaAtualCupons);
      mostrarToast("Cupom desativado com sucesso!", "success");
    } catch (error) {
      mostrarToast(error.message, "error");
      console.error(error);
    }
  }

  async function reativarCupom(id) {
    try {
      const res = await fetch(`/api/v1/cupons/${id}/reativar`, {
        method: "POST"
      });
      if (!res.ok) throw new Error("Erro ao reativar cupom.");

      carregarCupons(paginaAtualCupons);
      mostrarToast("Cupom reativado com sucesso!", "success");
    } catch (error) {
      mostrarToast(error.message, "error");
      console.error(error);
    }
  }

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

  carregarCupons(0);
});
