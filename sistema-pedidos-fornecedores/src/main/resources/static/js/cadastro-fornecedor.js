document.addEventListener("DOMContentLoaded", function () {
  const cnpjInput = document.getElementById("cnpj");

  // Aplica a máscara de formatação para o campo CNPJ
  if (cnpjInput) {
    cnpjInput.addEventListener("input", function (e) {
      let value = e.target.value.replace(/\D/g, "");
      value = value.substring(0, 14);

      // Aplica a formatação XX.XXX.XXX/XXXX-XX
      if (value.length > 12) {
        value = value.replace(
          /(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})/,
          "$1.$2.$3/$4-$5"
        );
      } else if (value.length > 8) {
        value = value.replace(/(\d{2})(\d{3})(\d{3})(\d{1,4})/, "$1.$2.$3/$4");
      } else if (value.length > 5) {
        value = value.replace(/(\d{2})(\d{3})(\d{1,3})/, "$1.$2.$3");
      } else if (value.length > 2) {
        value = value.replace(/(\d{2})(\d{1,3})/, "$1.$2");
      }
      e.target.value = value;
    });
  }

  const formFornecedor = document.getElementById("cadastro-fornecedor-form");

  // Lida com a submissão do formulário de fornecedor
  if (formFornecedor) {
    formFornecedor.addEventListener("submit", function (event) {
      // Previne o comportamento padrão de recarregar a página
      event.preventDefault();

      const formData = new FormData(formFornecedor);
      const data = Object.fromEntries(formData.entries());

      // Remove a formatação do CNPJ antes de enviar para a API
      if (data.cnpj) {
        data.cnpj = data.cnpj.replace(/\D/g, "");
      }

      fetch("/api/v1/fornecedores", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
      })
        .then((response) => {
          if (!response.ok) {
            // Se a resposta não for OK, rejeita a promise com o erro
            return response.json().then((err) => Promise.reject(err));
          }
          return response.json();
        })
        .then((data) => {
          mostrarMensagem(
            "Cadastro realizado com sucesso! Redirecionando para o login...",
            "success"
          );
          // Redireciona para a página de login após 2 segundos
          setTimeout(() => {
            window.location.href = "/login";
          }, 2000);
        })
        .catch((error) => {
          console.error("Erro no cadastro do fornecedor:", error);
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
