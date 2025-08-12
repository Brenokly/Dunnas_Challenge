document.addEventListener("DOMContentLoaded", () => {
  const token = localStorage.getItem("jwtToken");
  if (!token) {
    window.location.href = "/login";
    return;
  }
  // DOM elements
  const btnFiltrarCategorias = document.getElementById(
    "btn-filtrar-categorias"
  );
  const modalFiltro = document.getElementById("modal-filtro-categorias");
  const modalCategoriasList = document.getElementById("modal-categorias-list");
  const modalCancelBtn = document.getElementById("modal-cancel-btn");
  const modalApplyBtn = document.getElementById("modal-apply-btn");
  const productGrid = document.getElementById("product-grid");
  const paginacaoProdutos = document.getElementById("paginacao-produtos");

  // Estado
  let categorias = [];
  let categoriasSelecionadas = new Set();
  let categoriasSelecionadasModal = new Set();
  let paginaAtual = 0;
  const tamanhoPagina = 12;
  let carregandoPagina = true;

  // Modal Filtro Categorias
  function abrirModal() {
    modalFiltro.classList.add("active");
    btnFiltrarCategorias.setAttribute("aria-expanded", "true");
    categoriasSelecionadasModal = new Set(categoriasSelecionadas);
    renderizarCategoriasModal();
    modalFiltro.focus();
  }

  function fecharModal() {
    modalFiltro.classList.remove("active");
    btnFiltrarCategorias.setAttribute("aria-expanded", "false");
    btnFiltrarCategorias.focus();
  }

  modalFiltro.addEventListener("click", (e) => {
    if (e.target === modalFiltro) fecharModal();
  });

  document.addEventListener("keydown", (e) => {
    if (e.key === "Escape" && modalFiltro.classList.contains("active")) {
      fecharModal();
    }
  });

  function renderizarCategoriasModal() {
    modalCategoriasList.innerHTML = "";
    if (categorias.length === 0) {
      modalCategoriasList.innerHTML =
        "<p class='error-message'>Nenhuma categoria disponível.</p>";
      return;
    }
    categorias.forEach((cat) => {
      const label = document.createElement("label");
      label.htmlFor = `modal-cat-${cat.id}`;
      const checkbox = document.createElement("input");
      checkbox.type = "checkbox";
      checkbox.id = `modal-cat-${cat.id}`;
      checkbox.value = cat.id;
      checkbox.checked = categoriasSelecionadasModal.has(cat.id);
      checkbox.addEventListener("change", () => {
        if (checkbox.checked) {
          categoriasSelecionadasModal.add(cat.id);
        } else {
          categoriasSelecionadasModal.delete(cat.id);
        }
      });
      label.appendChild(checkbox);
      label.appendChild(document.createTextNode(cat.nome));
      modalCategoriasList.appendChild(label);
    });
  }

  function aplicarFiltros() {
    categoriasSelecionadas = new Set(categoriasSelecionadasModal);
    paginaAtual = 0;
    atualizarTextoBotaoFiltro();
    carregarProdutos();
    fecharModal();
  }

  function atualizarTextoBotaoFiltro() {
    const qtd = categoriasSelecionadas.size;
    btnFiltrarCategorias.innerHTML = `<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-filter"><polygon points="22 3 2 3 10 12.46 10 19 14 21 14 12.46 22 3"/></svg><span>Filtrar (${qtd})</span>`;
  }

  // Carregamento de categorias
  async function carregarCategorias() {
    try {
      const res = await fetch("/api/v1/categorias?page=0&size=100", {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (!res.ok) throw new Error("Erro ao carregar categorias");
      const data = await res.json();
      categorias = data.content || [];
    } catch (error) {
      categorias = [];
      alert("Erro ao carregar categorias. Tente recarregar a página.");
      console.error(error);
    }
  }

  // Carregamento de produtos
  async function carregarProdutos(pagina = 0) {
    paginaAtual = pagina;
    desabilitarComponentes(true);
    mostrarSpinnerProdutos();

    const categoriasQuery = Array.from(categoriasSelecionadas)
      .map((id) => `categorias=${id}`)
      .join("&");
    const url =
      `/api/v1/produtos?page=${pagina}&size=${tamanhoPagina}` +
      (categoriasQuery ? "&" + categoriasQuery : "");

    try {
      const res = await fetch(url, {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (!res.ok) throw new Error("Erro ao carregar produtos");
      const data = await res.json();
      renderizarProdutos(data.content);
      renderizarPaginacao(
        data.totalPages,
        paginaAtual,
        paginacaoProdutos,
        carregarProdutos
      );
    } catch (error) {
      productGrid.innerHTML =
        "<p class='error-message'>Erro ao carregar produtos.</p>";
      paginacaoProdutos.innerHTML = "";
      alert("Erro ao carregar produtos. Tente novamente.");
      console.error(error);
    } finally {
      desabilitarComponentes(false);
    }
  }

  function mostrarSpinnerProdutos() {
    productGrid.innerHTML = `<div class="spinner" aria-label="Carregando produtos"></div>`;
  }

  // Função unificada para desabilitar/habilitar componentes
  function desabilitarComponentes(flag) {
    btnFiltrarCategorias.disabled = flag;
    const botoesPaginacao = paginacaoProdutos.querySelectorAll("button");
    botoesPaginacao.forEach((btn) => (btn.disabled = flag));
  }

  // Renderização produtos
  function renderizarProdutos(produtos) {
    productGrid.innerHTML = "";
    if (!produtos || produtos.length === 0) {
      productGrid.innerHTML = `<p class="empty-message">Nenhum produto encontrado para o filtro selecionado.</p>`;
      return;
    }
    produtos.forEach((produto) => {
      const card = document.createElement("article");
      card.className = "product-card";
      card.setAttribute("role", "listitem");
      const precoFormatado = produto.preco.toFixed(2).replace(".", ",");
      const categoriasTags = document.createElement("div");
      categoriasTags.className = "category-list";
      produto.categorias.forEach((c) => {
        const tag = document.createElement("span");
        tag.className = "category-tag";
        tag.textContent = c.nome;
        categoriasTags.appendChild(tag);
      });
      const btnAdd = document.createElement("button");
      btnAdd.className = "add-to-cart-btn";
      btnAdd.textContent = "Adicionar ao Carrinho";
      btnAdd.disabled = false;
      btnAdd.setAttribute(
        "aria-label",
        `Adicionar ${produto.nome} ao carrinho`
      );
      btnAdd.dataset.produtoId = produto.id;
      btnAdd.dataset.produtoNome = produto.nome;
      btnAdd.dataset.preco = produto.preco;
      btnAdd.dataset.percentualDesconto = produto.percentualDesconto || 0;
      btnAdd.dataset.fornecedorId = produto.fornecedorId;
      card.appendChild(document.createElement("h3")).textContent = produto.nome;
      card.appendChild(categoriasTags);
      const precoEl = document.createElement("p");
      precoEl.className = "price";
      precoEl.textContent = `R$ ${precoFormatado}`;
      card.appendChild(precoEl);
      card.appendChild(btnAdd);
      productGrid.appendChild(card);
    });
  }
  // Renderizar paginação
  function renderizarPaginacao(
    totalPages,
    paginaAtual,
    container,
    onPageClick
  ) {
    container.innerHTML = "";
    if (totalPages <= 1) return;
    const btnPrev = document.createElement("button");
    btnPrev.textContent = "«";
    btnPrev.disabled = paginaAtual === 0;
    btnPrev.title = "Página anterior";
    btnPrev.addEventListener("click", () => onPageClick(paginaAtual - 1));
    container.appendChild(btnPrev);
    for (let i = 0; i < totalPages; i++) {
      const btn = document.createElement("button");
      btn.textContent = i + 1;
      btn.disabled = i === paginaAtual;
      btn.setAttribute("aria-current", i === paginaAtual ? "page" : false);
      btn.addEventListener("click", () => onPageClick(i));
      container.appendChild(btn);
    }
    const btnNext = document.createElement("button");
    btnNext.textContent = "»";
    btnNext.disabled = paginaAtual === totalPages - 1;
    btnNext.title = "Próxima página";
    btnNext.addEventListener("click", () => onPageClick(paginaAtual + 1));
    container.appendChild(btnNext);
  }

  // Eventos
  btnFiltrarCategorias.addEventListener("click", abrirModal);
  modalCancelBtn.addEventListener("click", fecharModal);
  modalApplyBtn.addEventListener("click", aplicarFiltros);

  (async function init() {
    desabilitarComponentes(true);
    await carregarCategorias();
    atualizarTextoBotaoFiltro();
    await carregarProdutos();
  })();
});
