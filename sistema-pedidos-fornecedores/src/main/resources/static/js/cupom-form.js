document.addEventListener("DOMContentLoaded", () => {
  const token = localStorage.getItem("jwtToken");
  if (!token) {
    window.location.href = "/login";
    return;
  }

  const form = document.getElementById("form-novo-cupom");

  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    const btnSubmit = document.getElementById("btn-submit");
    btnSubmit.disabled = true;
    btnSubmit.textContent = "Salvando...";

    const formData = new FormData(form);

    const valorMinimoPedido = formData.get("valorMinimoPedido");
    const limiteDeUsos = formData.get("limiteDeUsos");
    const dataValidadeStr = formData.get("dataValidade");

    // CORREÇÃO: Validação de data mais robusta com objeto Date
    const hoje = new Date();
    hoje.setHours(0, 0, 0, 0);
    const dataValidade = new Date(dataValidadeStr);
    dataValidade.setHours(0, 0, 0, 0);

    if (dataValidade < hoje) {
      mostrarToast("A data de validade não pode ser no passado.", "error");
      btnSubmit.disabled = false;
      btnSubmit.textContent = "Salvar Cupom";
      return;
    }

    const data = {
      codigo: formData.get("codigo"),
      tipoDesconto: formData.get("tipoDesconto"),
      valor: parseFloat(formData.get("valor")),
      dataValidade: dataValidadeStr,
      ...(valorMinimoPedido && {
        valorMinimoPedido: parseFloat(valorMinimoPedido),
      }),
      ...(limiteDeUsos && { limiteDeUsos: parseInt(limiteDeUsos) }),
    };

    try {
      const res = await fetch("/api/v1/cupons", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(data),
      });

      if (!res.ok) {
        const errorData = await res.json();
        throw new Error(errorData.message || "Erro ao cadastrar cupom.");
      }

      mostrarToast("Cupom cadastrado com sucesso!", "success");
      setTimeout(() => {
        window.location.href = "/fornecedor/cupons";
      }, 2000);
    } catch (error) {
      mostrarToast(error.message, "error");
      btnSubmit.disabled = false;
      btnSubmit.textContent = "Salvar Cupom";
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
});
