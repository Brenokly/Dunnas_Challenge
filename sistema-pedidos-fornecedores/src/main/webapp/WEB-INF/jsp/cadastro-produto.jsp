<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <!DOCTYPE html>
    <html lang="pt-br">

    <head>
      <meta charset="UTF-8" />
      <meta name="viewport" content="width=device-width, initial-scale=1" />
      <title>Novo Produto - Fornecedor</title>
      <link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/base.css" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components/forms.css" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components/buttons.css" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/components/messages.css" />
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pages/produto-form.css" />
    </head>

    <body>
      <%@ include file="includes/header.jsp" %>
        <div class="container-form" role="main">
          <h2>Cadastrar Novo Produto</h2>
          <form id="form-novo-produto" class="card-form">
            <div class="form-group">
              <label for="nome">Nome do Produto</label>
              <input type="text" id="nome" name="nome" required>
            </div>
            <div class="form-group">
              <label for="descricao">Descrição</label>
              <textarea id="descricao" name="descricao" rows="4"></textarea>
            </div>
            <div class="form-group">
              <label for="preco">Preço (R$)</label>
              <input type="number" id="preco" name="preco" step="0.01" min="0" required>
            </div>
            <div class="form-group">
              <label for="percentualDesconto">Desconto (%)</label>
              <input type="number" id="percentualDesconto" name="percentualDesconto" min="0" max="100" value="0"
                required>
            </div>

            <div class="form-group">
              <label>Categorias</label>
              <button type="button" class="btn secondary-btn" id="btn-selecionar-categorias">
                Selecionar Categorias
              </button>
              <div id="categorias-tags-container" class="tags-container">
              </div>
            </div>

            <div class="form-actions">
              <a href="<c:url value='/dashboard' />" class="btn secondary-btn">Cancelar</a>
              <button type="submit" id="btn-submit" class="btn primary-btn">Salvar Produto</button>
            </div>
          </form>
        </div>

        <div id="modal-selecionar-categorias" class="modal-backdrop" role="dialog" aria-modal="true"
          aria-labelledby="modal-titulo" tabindex="-1">
          <div class="modal-content">
            <h3 id="modal-titulo">Selecione as Categorias</h3>
            <div class="modal-categorias-list" id="modal-categorias-list" tabindex="0">
            </div>
            <div class="modal-buttons">
              <button type="button" class="btn secondary-btn" id="modal-cancel-btn">Cancelar</button>
              <button type="button" class="btn primary-btn" id="modal-apply-btn">Aplicar</button>
            </div>
          </div>
        </div>

        <%@ include file="includes/footer.jsp" %>
          <script src="${pageContext.request.contextPath}/js/cadastro-produto.js"></script>
    </body>

    </html>