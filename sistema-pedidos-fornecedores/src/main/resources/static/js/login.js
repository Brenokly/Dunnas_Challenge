document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("login-form");
  const errorDiv = form.nextElementSibling; // div para erros

  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    errorDiv.style.display = "none";
    errorDiv.textContent = "";

    const username = form.username.value.trim();
    const password = form.password.value;

    if (!username || !password) {
      errorDiv.textContent = "Por favor, preencha usuário e senha.";
      errorDiv.style.display = "block";
      return;
    }

    try {
      const response = await fetch("/api/v1/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({ usuario: username, senha: password }),
      });

      if (!response.ok) {
        let message = "Erro ao fazer login. Tente novamente.";
        if (response.status === 401) message = "Usuário ou senha inválidos.";
        errorDiv.textContent = message;
        errorDiv.style.display = "block";
        return;
      }

      const data = await response.json();
      const token = data.token;

      if (!token) {
        errorDiv.textContent = "Resposta inválida do servidor.";
        errorDiv.style.display = "block";
        return;
      }

      localStorage.setItem("jwtToken", token);
      window.location.href = "/dashboard";
    } catch (err) {
      console.error(err);
      errorDiv.textContent = "Erro na comunicação com o servidor.";
      errorDiv.style.display = "block";
    }
  });
});
