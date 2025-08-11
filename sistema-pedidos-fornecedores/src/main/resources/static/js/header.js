document.addEventListener("DOMContentLoaded", function () {
  const userMenu = document.querySelector(".user-menu");
  let timeoutId;

  if (userMenu) {
    // Evento para quando o mouse ENTRA na área do menu (incluindo o dropdown)
    userMenu.addEventListener("mouseenter", function () {
      // Cancela qualquer "agendamento" para esconder o menu que possa existir
      clearTimeout(timeoutId);
      // Adiciona a classe que torna o dropdown visível
      userMenu.classList.add("dropdown-active");
    });

    // Evento para quando o mouse SAI da área do menu
    userMenu.addEventListener("mouseleave", function () {
      // Agenda o desaparecimento do menu após um pequeno atraso (300ms)
      // Isso dá tempo para o usuário mover o cursor para o dropdown.
      timeoutId = setTimeout(() => {
        userMenu.classList.remove("dropdown-active");
      }, 300);
    });
  }
});
