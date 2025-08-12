document.addEventListener("DOMContentLoaded", function () {
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

  document.addEventListener("click", function (event) {
    const button = event.target.closest(".add-to-cart-btn");
    if (!button) return;

    const produtoId = button.dataset.produtoId;
    const produtoNome = button.dataset.produtoNome;
    const produtoPreco = parseFloat(button.dataset.preco);
    const produtoPercentualDesconto =
      parseFloat(button.dataset.percentualDesconto) || 0;
    const fornecedorId = button.dataset.fornecedorId;

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
        fornecedorId: fornecedorId,
      });
    }

    localStorage.setItem("shoppingCart", JSON.stringify(cart));
    showToast(`${produtoNome} foi adicionado ao carrinho!`);
    updateCartCounter();
  });

  // A função `updateCartCounter` precisa ser globalmente acessível
  window.updateCartCounter = function () {
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
  };

  function showToast(message) {
    const toast = document.getElementById("toast");
    if (!toast) return;

    toast.textContent = message;
    toast.classList.add("show");

    setTimeout(() => {
      toast.classList.remove("show");
    }, 2500);
  }

  updateCartCounter();
});
