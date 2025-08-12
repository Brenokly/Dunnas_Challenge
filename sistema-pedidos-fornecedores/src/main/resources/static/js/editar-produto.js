document.addEventListener("DOMContentLoaded", () => {
  const token = localStorage.getItem("jwtToken");
  if (!token) {
    window.location.href = "/login";
    return;
  }

  // O ID do produto será extraído da URL
  const produtoId = window.location.pathname.split("/").pop();
  const form = document.getElementById("form-editar-produto");
  const categoriasList = document.getElementById("categorias-list");

  // Altera o título do formulário para edição
  document.querySelector(".container-form h2").textContent = "Editar Produto";

  async function carregarDadosProduto() {
    try {
      const res = await fetch(`/api/v1/produtos/${produtoId}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (!res.ok)
        throw new Error("Produto não encontrado ou você não tem permissão.");

      const produto = await res.json();
      preencherFormulario(produto);
      carregarCategorias(produto.categorias.map((c) => c.id));
    } catch (error) {
      mostrarToast(error.message, "error");
      console.error(error);
    }
  }

  function preencherFormulario(produto) {
    document.getElementById("nome").value = produto.nome;
    document.getElementById("descricao").value = produto.descricao || "";
    document.getElementById("preco").value = produto.preco;
    document.getElementById("percentualDesconto").value =
      produto.percentualDesconto;
  }

  async function carregarCategorias(categoriasProduto) {
    try {
      const res = await fetch("/api/v1/categorias?page=0&size=100", {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (!res.ok) throw new Error("Erro ao carregar categorias.");

      const data = await res.json();
      renderizarCategorias(data.content, categoriasProduto);
    } catch (error) {
      categoriasList.innerHTML = `<p class="error-message">Erro ao carregar categorias.</p>`;
      console.error(error);
    }
  }

  function renderizarCategorias(categorias, categoriasProduto) {
    categoriasList.innerHTML = "";
    categorias.forEach((cat) => {
      const label = document.createElement("label");
      const isChecked = categoriasProduto.includes(cat.id);
      label.innerHTML = `
                <input type="checkbox" name="categoriaIds" value="${cat.id}" ${
        isChecked ? "checked" : ""
      } />
                ${cat.nome}
            `;
      categoriasList.appendChild(label);
    });
  }

  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    const btnSubmit = document.getElementById("btn-submit");
    btnSubmit.disabled = true;
    btnSubmit.textContent = "Salvando...";

    const formData = new FormData(form);
    const data = {
      nome: formData.get("nome"),
      descricao: formData.get("descricao"),
      preco: parseFloat(formData.get("preco")),
      percentualDesconto: parseFloat(formData.get("percentualDesconto")),
      categoriaIds: Array.from(formData.getAll("categoriaIds")),
    };

    try {
      const res = await fetch(`/api/v1/produtos/${produtoId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(data),
      });

      if (!res.ok) {
        const errorData = await res.json();
        throw new Error(errorData.message || "Erro ao atualizar produto.");
      }

      mostrarToast("Produto atualizado com sucesso!", "success");

      // CORREÇÃO: Adiciona um atraso de 2 segundos antes de redirecionar
      setTimeout(() => {
        window.location.href = "/dashboard";
      }, 2000); // 2000ms = 2 segundos
    } catch (error) {
      mostrarToast(error.message, "error");
      btnSubmit.disabled = false;
      btnSubmit.textContent = "Salvar Alterações";
    }
  });

  carregarDadosProduto();

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
});
