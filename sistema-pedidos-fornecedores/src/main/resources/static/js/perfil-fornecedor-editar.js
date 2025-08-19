document.addEventListener("DOMContentLoaded", () => {
  const formSenha = document.getElementById("edit-password-form");
  const fornecedorId = formSenha.closest("[data-fornecedor-id]").dataset
    .fornecedorId;

  formSenha.addEventListener("submit", async (e) => {
    e.preventDefault();
    const btnAlterarSenha = formSenha.querySelector("button[type='submit']");
    btnAlterarSenha.disabled = true;

    const senhaAtual = document.getElementById("senhaAtual").value;
    const novaSenha = document.getElementById("novaSenha").value;
    const confirmacaoNovaSenha = document.getElementById(
      "confirmacaoNovaSenha"
    ).value;

    if (novaSenha !== confirmacaoNovaSenha) {
      mostrarToast("A nova senha e a confirmação não coincidem.", "error");
      btnAlterarSenha.disabled = false;
      return;
    }
    if (novaSenha.length < 6) {
      mostrarToast("A nova senha deve ter no mínimo 6 caracteres.", "error");
      btnAlterarSenha.disabled = false;
      return;
    }

    // CORREÇÃO: Inclui o campo 'confirmacaoNovaSenha' no objeto JSON
    const data = {
      senhaAtual: senhaAtual,
      novaSenha: novaSenha,
      confirmacaoNovaSenha: confirmacaoNovaSenha,
    };

    try {
      const res = await fetch(`/api/v1/fornecedores/${fornecedorId}/senha`, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(data),
      });

      if (!res.ok) {
        const errorData = await res.json();
        throw new Error(errorData.message || "Erro ao alterar a senha.");
      }

      mostrarToast("Senha alterada com sucesso!", "success");
      setTimeout(() => {
        window.location.href = "/fornecedor/perfil";
      }, 2000);
    } catch (error) {
      mostrarToast(error.message, "error");
      btnAlterarSenha.disabled = false;
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
