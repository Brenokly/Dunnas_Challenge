<%-- Em /WEB-INF/jsp/dashboard.jsp --%>
  <%@ page ... %>
    <%@ taglib ... %>
      <!DOCTYPE html>
      <html>

      <head> ... </head>

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
                    <p class="category">${produto.categoria()}</p>
                    <button>Adicionar ao Carrinho</button>
                  </div>
                </c:forEach>
              </div>
              <c:if test="${paginaDeProdutos.empty}">
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
                    <th>Categoria</th>
                    <th>Preço</th>
                    <th>Status</th>
                    <th>Ações</th>
                  </tr>
                </thead>
                <tbody>
                  <c:forEach items="${paginaDeProdutos.content}" var="produto">
                    <tr>
                      <td>${produto.nome()}</td>
                      <td>${produto.categoria()}</td>
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
              <c:if test="${paginaDeProdutos.empty}">
                <div class="empty-list-message">
                  <p>Você ainda não cadastrou nenhum produto. <a href="#">Clique aqui para começar!</a></p>
                </div>
              </c:if>
            </c:if>

          </div>

          <%@ include file="includes/footer.jsp" %>
      </body>

      </html>