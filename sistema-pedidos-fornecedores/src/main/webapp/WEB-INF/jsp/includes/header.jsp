<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

      <header>
        <h1><a href="<c:url value='/' />">Sistema de Pedidos e Pagamentos</a></h1>

        <nav>
          <%-- O menu do usuário só é renderizado se o usuário estiver autenticado --%>
            <sec:authorize access="isAuthenticated()">
              <div class="user-menu">
                <%-- Acessa o username do principal, que agora sabemos que existe --%>
                  <span class="username">Olá,
                    <sec:authentication property="principal.username" />
                  </span>

                  <div class="dropdown-content">
                    <%-- Verifica se o principal é uma instância da classe Cliente --%>
                      <sec:authorize
                        access="principal instanceof T(br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Cliente)">
                        <a href="#">Meu Perfil de Cliente</a>
                      </sec:authorize>

                      <%-- Verifica se o principal é uma instância da classe Fornecedor --%>
                        <sec:authorize
                          access="principal instanceof T(br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Fornecedor)">
                          <a href="#">Meu Perfil de Fornecedor</a>
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