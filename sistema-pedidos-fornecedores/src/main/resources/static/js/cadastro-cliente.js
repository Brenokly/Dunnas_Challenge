document.addEventListener("DOMContentLoaded", function () {
  const cpfInput = document.getElementById("cpf");

  // Aplica a máscara de formatação para o campo CPF
  if (cpfInput) {
    cpfInput.addEventListener("input", function (e) {
      let value = e.target.value.replace(/\D/g, "");
      value = value.substring(0, 11);

      // Aplica a formatação XXX.XXX.XXX-XX
      if (value.length > 9) {
        value = value.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, "$1.$2.$3-$4");
      } else if (value.length > 6) {
        value = value.replace(/(\d{3})(\d{3})(\d{1,3})/, "$1.$2.$3");
      } else if (value.length > 3) {
        value = value.replace(/(\d{3})(\d{1,3})/, "$1.$2");
      }
      e.target.value = value;
    });
  }

  const formCliente = document.getElementById("cadastro-cliente-form");

  // Lida com a submissão do formulário de cliente
  if (formCliente) {
    formCliente.addEventListener("submit", function (event) {
      // Previne o comportamento padrão de recarregar a página
      event.preventDefault();

      const formData = new FormData(formCliente);
      const data = Object.fromEntries(formData.entries());

      // Remove a formatação do CPF antes de enviar para a API
      if (data.cpf) {
        data.cpf = data.cpf.replace(/\D/g, "");
      }

      fetch("/api/v1/clientes", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
      })
        .then((response) => {
          if (!response.ok) {
            return response.json().then((err) => Promise.reject(err));
          }
          return response.json();
        })
        .then((data) => {
          mostrarMensagem(
            "Cadastro realizado com sucesso! Redirecionando para o login...",
            "success"
          );

          setTimeout(() => {
            window.location.href = "/login";
          }, 2000);
        })
        .catch((error) => {
          console.error("Erro no cadastro do cliente:", error);
          mostrarMensagem(
            error.message || "Ocorreu um erro no cadastro.",
            "error"
          );
        });
    });
  }

  function mostrarMensagem(mensagem, tipo) {
    const feedbackDiv = document.getElementById("feedback-message");
    if (feedbackDiv) {
      feedbackDiv.textContent = mensagem;
      feedbackDiv.className =
        tipo === "success" ? "success-message" : "error-message";
      feedbackDiv.style.display = "block";
    }
  }
});
