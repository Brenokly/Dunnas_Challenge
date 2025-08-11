<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <!DOCTYPE html>
    <html lang="pt-br">

    <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <title>Dashboard - Sistema de Pedidos</title>
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
      <link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico">
    </head>

    <body>

      <%@ include file="includes/header.jsp" %>

        <div class="dashboard-container">
          <c:if test="${userType == 'cliente'}">
            <h2>Vitrine de Produtos</h2>
            <div class="product-grid">
              <c:forEach items="${paginaDeProdutos.content}" var="produto">
                <div class="product-card">
                  <h3>${produto.nome()}</h3>
                  <p class="price">R$ ${produto.preco()}</p>
                  <div class="category-list">
                    <c:forEach items="${produto.categorias()}" var="categoria">
                      <span class="category-tag">${categoria.nome()}</span>
                    </c:forEach>
                  </div>
                  <button>Adicionar ao Carrinho</button>
                </div>
              </c:forEach>
            </div>
            <%-- CORREÇÃO AQUI --%>
              <c:if test="${paginaDeProdutos.isEmpty()}">
                <div class="empty-list-message">
                  <p>Nenhum produto disponível no momento. Volte mais tarde!</p>
                </div>
              </c:if>
          </c:if>

          <c:if test="${userType == 'fornecedor'}">
            <h2>Meus Produtos</h2>
            <table class="product-table">
              <thead>
                <tr>
                  <th>Nome</th>
                  <th>Categorias</th>
                  <th>Preço</th>
                  <th>Status</th>
                  <th>Ações</th>
                </tr>
              </thead>
              <tbody>
                <c:forEach items="${paginaDeProdutos.content}" var="produto">
                  <tr>
                    <td>${produto.nome()}</td>
                    <td>
                      <c:forEach items="${produto.categorias()}" var="categoria" varStatus="loop">
                        ${categoria.nome()}<c:if test="${not loop.last}">, </c:if>
                      </c:forEach>
                    </td>
                    <td>R$ ${produto.preco()}</td>
                    <td>${produto.ativo() ? 'Ativo' : 'Inativo'}</td>
                    <td>
                      <a href="#" class="action-link">Editar</a>
                      <a href="#" class="action-link delete">Desativar</a>
                    </td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
            <c:if test="${paginaDeProdutos.isEmpty()}">
              <div class="empty-list-message">
                <p>Você ainda não cadastrou nenhum produto. <a href="#">Clique aqui para começar!</a></p>
              </div>
            </c:if>
          </c:if>
        </div>

        <%@ include file="includes/footer.jsp" %>
    </body>

    </html>