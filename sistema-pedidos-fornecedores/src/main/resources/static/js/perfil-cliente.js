document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("add-saldo-form");
  const inputValor = document.getElementById("valor");
  const saldoValorSpan = document.getElementById("saldo-valor");
  const feedbackDiv = document.getElementById("feedback-message");

  form.addEventListener("submit", async (event) => {
    event.preventDefault();

    const valorStr = inputValor.value.trim();
    if (!valorStr || isNaN(valorStr) || parseFloat(valorStr) <= 0) {
      mostrarMensagem("Informe um valor válido maior que zero.", "error");
      return;
    }

    const valor = parseFloat(valorStr).toFixed(2);

    // Desabilitar botão e input enquanto processa
    inputValor.disabled = true;
    form.querySelector("button[type=submit]").disabled = true;

    try {
      const response = await fetch("/api/v1/clientes/meu-perfil/saldo", {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({ valor: valor }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Erro ao adicionar saldo.");
      }

      const clienteAtualizado = await response.json();

      // Atualizar saldo na tela (formato brasileiro com vírgula)
      saldoValorSpan.textContent = `R$ ${clienteAtualizado.saldo
        .toFixed(2)
        .replace(".", ",")}`;
      mostrarMensagem(
        `Saldo atualizado com sucesso! Novo saldo: R$ ${clienteAtualizado.saldo
          .toFixed(2)
          .replace(".", ",")}`,
        "success"
      );

      // Limpar input
      inputValor.value = "";
    } catch (error) {
      mostrarMensagem(error.message, "error");
    } finally {
      inputValor.disabled = false;
      form.querySelector("button[type=submit]").disabled = false;
    }
  });

  function mostrarMensagem(mensagem, tipo) {
    feedbackDiv.textContent = mensagem;
    feedbackDiv.className =
      tipo === "success" ? "success-message" : "error-message";
    feedbackDiv.style.display = "block";
    setTimeout(() => {
      feedbackDiv.style.display = "none";
    }, 5000);
  }
});
