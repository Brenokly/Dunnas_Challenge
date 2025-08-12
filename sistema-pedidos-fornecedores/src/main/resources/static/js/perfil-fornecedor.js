document.addEventListener("DOMContentLoaded", () => {
  const token = localStorage.getItem("jwtToken");
  if (!token) {
    window.location.href = "/login";
    return;
  }

  const formDesativar = document.getElementById("delete-account-form");
  const fornecedorId = formDesativar.dataset.fornecedorId;

  formDesativar.addEventListener("submit", async (e) => {
    e.preventDefault();
    if (
      !confirm(
        "Tem certeza que deseja desativar sua conta? Esta ação é irreversível."
      )
    ) {
      return;
    }

    try {
      const res = await fetch(`/api/v1/fornecedores/${fornecedorId}`, {
        method: "DELETE",
        headers: { Authorization: `Bearer ${token}` },
      });

      if (!res.ok) throw new Error("Erro ao desativar a conta.");

      localStorage.removeItem("jwtToken");
      mostrarToast(
        "Conta desativada com sucesso. Você será redirecionado.",
        "success"
      );
      setTimeout(() => {
        window.location.href = "/login";
      }, 2000);
    } catch (error) {
      mostrarToast(error.message, "error");
      console.error(error);
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
