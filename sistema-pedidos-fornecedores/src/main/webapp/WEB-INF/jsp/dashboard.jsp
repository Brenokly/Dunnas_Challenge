<%-- /WEB-INF/jsp/dashboard.jsp --%>
  <jsp:include page="includes/header.jsp" />
  <div class="container">
    <h2>Bem-vindo ao Dashboard!</h2>
    <p>Você está logado com sucesso.</p>
    <%-- Adicione um link de logout --%>
      <form action="<c:url value='/logout' />" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <button type="submit">Sair</button>
      </form>
  </div>
  <jsp:include page="includes/footer.jsp" />