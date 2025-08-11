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
});
