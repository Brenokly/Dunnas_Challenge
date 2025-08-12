document.addEventListener("DOMContentLoaded", () => {
  const token = localStorage.getItem("jwtToken");
  if (!token) {
    window.location.href = "/login";
    return;
  }

  const form = document.getElementById("form-novo-produto");
  const categoriasList = document.getElementById("modal-categorias-list");
  const btnSelecionarCategorias = document.getElementById(
    "btn-selecionar-categorias"
  );
  const modal = document.getElementById("modal-selecionar-categorias");
  const modalApplyBtn = document.getElementById("modal-apply-btn");
  const modalCancelBtn = document.getElementById("modal-cancel-btn");
  const tagsContainer = document.getElementById("categorias-tags-container");

  let todasCategorias = [];
  let categoriasSelecionadas = new Set();
  let categoriasSelecionadasModal = new Set();

  // Lógica da Modal
  function abrirModal() {
    modal.classList.add("active");
    categoriasSelecionadasModal = new Set(categoriasSelecionadas);
    renderizarCategoriasModal();
  }

  function fecharModal() {
    modal.classList.remove("active");
  }

  function renderizarCategoriasModal() {
    categoriasList.innerHTML = "";
    todasCategorias.forEach((cat) => {
      const label = document.createElement("label");
      const isChecked = categoriasSelecionadasModal.has(cat.id);
      label.innerHTML = `
                <input type="checkbox" data-id="${cat.id}" ${
        isChecked ? "checked" : ""
      } />
                ${cat.nome}
            `;
      categoriasList.appendChild(label);
    });

    // Adiciona event listener para as checkboxes do modal
    categoriasList
      .querySelectorAll('input[type="checkbox"]')
      .forEach((checkbox) => {
        checkbox.addEventListener("change", () => {
          const id = checkbox.dataset.id;
          if (checkbox.checked) {
            categoriasSelecionadasModal.add(id);
          } else {
            categoriasSelecionadasModal.delete(id);
          }
        });
      });
  }

  function renderizarTags() {
    tagsContainer.innerHTML = "";
    categoriasSelecionadas.forEach((id) => {
      const categoria = todasCategorias.find((c) => c.id === id);
      if (categoria) {
        const tag = document.createElement("span");
        tag.className = "category-tag";
        tag.innerHTML = `
                    <span>${categoria.nome}</span>
                    <button type="button" class="remove-tag-btn" data-id="${id}">&times;</button>
                `;
        tagsContainer.appendChild(tag);
      }
    });

    // Adiciona event listener para o botão de remover tag
    tagsContainer.querySelectorAll(".remove-tag-btn").forEach((btn) => {
      btn.addEventListener("click", () => {
        const id = btn.dataset.id;
        categoriasSelecionadas.delete(id);
        renderizarTags();
      });
    });
  }

  // Lógica de Carregamento de Dados
  async function carregarCategorias() {
    try {
      const res = await fetch("/api/v1/categorias?page=0&size=100", {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (!res.ok) throw new Error("Erro ao carregar categorias.");

      const data = await res.json();
      todasCategorias = data.content || [];
    } catch (error) {
      tagsContainer.innerHTML = `<p class="error-message">Erro ao carregar categorias.</p>`;
      console.error(error);
    }
  }

  // Lógica de Formulário
  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    const btnSubmit = document.getElementById("btn-submit");
    btnSubmit.disabled = true;
    btnSubmit.textContent = "Salvando...";

    if (categoriasSelecionadas.size === 0) {
      mostrarToast("Selecione pelo menos uma categoria.", "error");
      btnSubmit.disabled = false;
      btnSubmit.textContent = "Salvar Produto";
      return;
    }

    const formData = new FormData(form);
    const data = {
      nome: formData.get("nome"),
      descricao: formData.get("descricao"),
      preco: parseFloat(formData.get("preco")),
      percentualDesconto: parseInt(formData.get("percentualDesconto")),
      categoriaIds: Array.from(categoriasSelecionadas),
    };

    try {
      const res = await fetch("/api/v1/produtos", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(data),
      });

      if (!res.ok) {
        const errorData = await res.json();
        throw new Error(errorData.message || "Erro ao cadastrar produto.");
      }

      mostrarToast("Produto cadastrado com sucesso!", "success");
      setTimeout(() => {
        window.location.href = "/dashboard";
      }, 2000);
    } catch (error) {
      mostrarToast(error.message, "error");
      btnSubmit.disabled = false;
      btnSubmit.textContent = "Salvar Produto";
    }
  });

  // Event Listeners
  btnSelecionarCategorias.addEventListener("click", abrirModal);
  modalApplyBtn.addEventListener("click", () => {
    categoriasSelecionadas = new Set(categoriasSelecionadasModal);
    renderizarTags();
    fecharModal();
  });
  modalCancelBtn.addEventListener("click", fecharModal);

  modal.addEventListener("click", (e) => {
    if (e.target === modal) fecharModal();
  });

  document.addEventListener("keydown", (e) => {
    if (e.key === "Escape" && modal.classList.contains("active")) {
      fecharModal();
    }
  });

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

  carregarCategorias();
});
