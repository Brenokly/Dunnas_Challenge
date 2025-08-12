document.addEventListener("DOMContentLoaded", function () {
  // Dropdown menu usuário
  const userMenu = document.querySelector(".user-menu");
  let timeoutId;

  if (userMenu) {
    userMenu.addEventListener("mouseenter", function () {
      clearTimeout(timeoutId);
      userMenu.classList.add("dropdown-active");
    });

    userMenu.addEventListener("mouseleave", function () {
      timeoutId = setTimeout(() => {
        userMenu.classList.remove("dropdown-active");
      }, 300);
    });
  }

  // Delegação de evento: captura clique em botões add-to-cart-btn em qualquer lugar
  document.addEventListener("click", function (event) {
    const button = event.target.closest(".add-to-cart-btn");
    if (!button) return; // não é botão add-to-cart

    const produtoId = button.dataset.produtoId;
    const produtoNome = button.dataset.produtoNome;
    const produtoPreco = parseFloat(button.dataset.preco);
    const produtoPercentualDesconto =
      parseFloat(button.dataset.percentualDesconto) || 0;

    let cart = JSON.parse(localStorage.getItem("shoppingCart")) || [];
    const existingProductIndex = cart.findIndex(
      (item) => item.id === produtoId
    );

    if (existingProductIndex > -1) {
      cart[existingProductIndex].quantidade++;
    } else {
      cart.push({
        id: produtoId,
        nome: produtoNome,
        preco: produtoPreco,
        percentualDesconto: produtoPercentualDesconto,
        quantidade: 1,
      });
    }

    localStorage.setItem("shoppingCart", JSON.stringify(cart));

    showToast(`${produtoNome} foi adicionado ao carrinho!`);
    updateCartCounter();
  });

  function updateCartCounter() {
    const cart = JSON.parse(localStorage.getItem("shoppingCart")) || [];
    const cartCounter = document.getElementById("cart-counter");

    if (cartCounter) {
      const totalItems = cart.reduce((sum, item) => sum + item.quantidade, 0);

      if (totalItems > 0) {
        cartCounter.textContent = totalItems;
        cartCounter.style.display = "flex";
      } else {
        cartCounter.style.display = "none";
      }
    }
  }

  function showToast(message) {
    const toast = document.getElementById("toast");
    if (!toast) return; // evitar erro se não existir toast

    toast.textContent = message;
    toast.classList.add("show");

    setTimeout(() => {
      toast.classList.remove("show");
    }, 2500);
  }

  updateCartCounter();
});
