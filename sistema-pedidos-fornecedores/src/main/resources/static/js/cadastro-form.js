document.addEventListener("DOMContentLoaded", function () {
  const cpfInput = document.getElementById("cpf");

  if (cpfInput) {
    cpfInput.addEventListener("input", function (e) {
      let value = e.target.value.replace(/\D/g, "");
      value = value.substring(0, 11);

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

  const cnpjInput = document.getElementById("cnpj");
  if (cnpjInput) {
    cnpjInput.addEventListener("input", function (e) {
      let value = e.target.value.replace(/\D/g, "");
      value = value.substring(0, 14);

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

  const formCliente = document.getElementById("cadastro-cliente-form");
  if (formCliente) {
    formCliente.addEventListener("submit", function (event) {
      // Previne o comportamento padrão de recarregar a página
      event.preventDefault();

      // Coleta os dados do formulário
      const formData = new FormData(formCliente);
      const data = Object.fromEntries(formData.entries());

      // Remove a formatação do CPF antes de enviar
      if (data.cpf) {
        data.cpf = data.cpf.replace(/\D/g, "");
      }

      // Envia os dados para a API
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
          }, 2000); // Atraso de 2 segundos
        })
        .catch((error) => {
          console.error("Erro no cadastro:", error);
          mostrarMensagem(
            error.message || "Ocorreu um erro no cadastro.",
            "error"
          );
        });
    });
  }

  const formFornecedor = document.getElementById("cadastro-fornecedor-form");
  if (formFornecedor) {
    formFornecedor.addEventListener("submit", function (event) {
      event.preventDefault();
      const formData = new FormData(formFornecedor);
      const data = Object.fromEntries(formData.entries());

      // Remove a formatação do CNPJ antes de enviar
      if (data.cnpj) {
        data.cnpj = data.cnpj.replace(/\D/g, "");
      }

      // Envia os dados para a API
      fetch("/api/v1/fornecedores", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
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
          console.error("Erro no cadastro:", error);
          mostrarMensagem(
            error.message || "Ocorreu um erro no cadastro.",
            "error"
          );
        });
    });
  }

  // Função utilitária para mostrar mensagens de feedback na tela
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
