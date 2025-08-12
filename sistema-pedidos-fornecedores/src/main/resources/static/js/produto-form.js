document.addEventListener("DOMContentLoaded", () => {
  const token = localStorage.getItem("jwtToken");
  if (!token) {
    window.location.href = "/login";
    return;
  }

  const form = document.getElementById("form-novo-produto");
  const categoriasList = document.getElementById("categorias-list");

  async function carregarCategorias() {
    try {
      const res = await fetch("/api/v1/categorias?page=0&size=100", {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (!res.ok) throw new Error("Erro ao carregar categorias.");

      const data = await res.json();
      renderizarCategorias(data.content);
    } catch (error) {
      categoriasList.innerHTML = `<p class="error-message">Erro ao carregar categorias. Tente recarregar a página.</p>`;
      console.error(error);
    }
  }

  function renderizarCategorias(categorias) {
    categoriasList.innerHTML = "";
    categorias.forEach((cat) => {
      const label = document.createElement("label");
      label.innerHTML = `
                <input type="checkbox" name="categoriaIds" value="${cat.id}" />
                ${cat.nome}
            `;
      categoriasList.appendChild(label);
    });
  }

  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    const btnSubmit = document.getElementById("btn-submit");
    btnSubmit.disabled = true;

    const formData = new FormData(form);
    const data = {
      nome: formData.get("nome"),
      descricao: formData.get("descricao"),
      preco: parseFloat(formData.get("preco")),
      percentualDesconto: parseInt(formData.get("percentualDesconto")),
      categoriaIds: Array.from(formData.getAll("categoriaIds")),
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
      window.location.href = "/dashboard";
    } catch (error) {
      mostrarToast(error.message, "error");
      btnSubmit.disabled = false;
    }
  });

  carregarCategorias();

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
