document.addEventListener("DOMContentLoaded", function () {
  const profileForm = document.getElementById("edit-profile-form");
  const passwordForm = document.getElementById("edit-password-form");
  const feedbackDiv = document.getElementById("feedback-message");
  const container = document.querySelector(".profile-container");
  const clienteId = container.dataset.clienteId;
  const token = localStorage.getItem("jwtToken");

  if (!token) {
    window.location.href = "/login";
  }

  if (profileForm) {
    profileForm.addEventListener("submit", function (event) {
      event.preventDefault();
      const formData = new FormData(profileForm);
      const data = {
        nome: formData.get("nome"),
        dataNascimento: formData.get("dataNascimento"),
      };

      fetch(`/api/v1/clientes/${clienteId}/dados-pessoais`, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(data),
      })
        .then(handleResponse)
        .then((data) => {
          mostrarMensagem(
            "Dados atualizados com sucesso! Redirecionando...",
            "success"
          );
          setTimeout(() => (window.location.href = "/cliente/perfil"), 1500);
        })
        .catch(handleError);
    });
  }

  if (passwordForm) {
    passwordForm.addEventListener("submit", function (event) {
      event.preventDefault();
      const formData = new FormData(passwordForm);
      const data = Object.fromEntries(formData.entries());

      if (data.novaSenha !== data.confirmacaoNovaSenha) {
        mostrarMensagem(
          "A nova senha e a confirmação não correspondem.",
          "error"
        );
        return;
      }

      fetch(`/api/v1/clientes/${clienteId}/senha`, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(data),
      })
        .then((response) => {
          // Resposta 204 não tem corpo
          if (!response.ok)
            return response.json().then((err) => Promise.reject(err));
          return Promise.resolve();
        })
        .then(() => {
          mostrarMensagem("Senha alterada com sucesso!", "success");
          passwordForm.reset();
        })
        .catch(handleError);
    });
  }

  function mostrarMensagem(mensagem, tipo) {
    if (feedbackDiv) {
      feedbackDiv.textContent = mensagem;
      feedbackDiv.className =
        tipo === "success" ? "success-message" : "error-message";
      feedbackDiv.style.display = "block";
    }
  }

  function handleResponse(response) {
    if (!response.ok) {
      return response.json().then((err) => Promise.reject(err));
    }
    return response.json();
  }

  function handleError(error) {
    console.error("Erro:", error);
    mostrarMensagem(error.message || "Ocorreu um erro.", "error");
  }
});
