<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <!DOCTYPE html>
    <html lang="pt-br">

    <head>
      <meta charset="UTF-8" />
      <meta name="viewport" content="width=device-width, initial-scale=1" />
      <title>Novo Cupom - Fornecedor</title>
      <link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components/forms.css" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components/buttons.css" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components/messages.css" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pages/cupom-form.css" />
    </head>

    <body>
      <%@ include file="includes/header.jsp" %>
        <div class="container-form" role="main">
          <h2>Cadastrar Novo Cupom</h2>
          <form id="form-novo-cupom" class="card-form">
            <div class="form-group">
              <label for="codigo">Código do Cupom</label>
              <input type="text" id="codigo" name="codigo" required>
            </div>
            <div class="form-group">
              <label for="tipoDesconto">Tipo de Desconto</label>
              <select id="tipoDesconto" name="tipoDesconto" required>
                <option value="P">Percentual (%)</option>
                <option value="F">Valor Fixo (R$)</option>
              </select>
            </div>
            <div class="form-group">
              <label for="valor">Valor do Desconto</label>
              <input type="number" id="valor" name="valor" step="0.01" min="0" required>
            </div>
            <div class="form-group">
              <label for="dataValidade">Data de Validade</label>
              <input type="date" id="dataValidade" name="dataValidade" required>
            </div>
            <div class="form-group">
              <label for="valorMinimoPedido">Valor Mínimo do Pedido (opcional)</label>
              <input type="number" id="valorMinimoPedido" name="valorMinimoPedido" step="0.01" min="0">
            </div>
            <div class="form-group">
              <label for="limiteDeUsos">Limite de Usos (opcional)</label>
              <input type="number" id="limiteDeUsos" name="limiteDeUsos" min="1">
            </div>
            <div class="form-actions">
              <a href="<c:url value='/fornecedor/cupons' />" class="btn secondary-btn">Cancelar</a>
              <button type="submit" id="btn-submit" class="btn primary-btn">Salvar Cupom</button>
            </div>
          </form>
        </div>
        <div id="toast-message" class="toast"></div>

        <%@ include file="includes/footer.jsp" %>
          <script src="${pageContext.request.contextPath}/js/cadastro-cupom.js"></script>
    </body>

    </html>