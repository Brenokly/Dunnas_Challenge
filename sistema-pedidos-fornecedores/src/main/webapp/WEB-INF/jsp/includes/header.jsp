<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

      <script src="${pageContext.request.contextPath}/js/header.js" defer></script>
      <header>
        <h1><a href="<c:url value='/' />">Sistema de Pedidos e Pagamentos</a></h1>

        <nav>
          <sec:authorize access="isAuthenticated()">
            <div class="user-menu">
              <span class="username">Olá,
                <sec:authentication property="principal.username" />
              </span>
              <div class="dropdown-content">

                <sec:authorize
                  access="principal instanceof T(br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Cliente)">
                  <a href="<c:url value='/cliente/perfil' />">Meu Perfil</a>
                </sec:authorize>

                <sec:authorize
                  access="principal instanceof T(br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Fornecedor)">
                  <a href="<c:url value='/fornecedor/perfil' />">Meu Perfil</a>
                </sec:authorize>

                <form action="<c:url value='/logout' />" method="post" class="logout-form">
                  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                  <button type="submit" class="logout-button">Sair</button>
                </form>
              </div>
            </div>
          </sec:authorize>
        </nav>
      </header>
      <main>