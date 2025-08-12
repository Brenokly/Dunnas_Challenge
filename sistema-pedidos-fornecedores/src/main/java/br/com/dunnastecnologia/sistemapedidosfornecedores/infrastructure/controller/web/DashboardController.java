package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.controller.web;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases.ProdutoUseCases;
import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Cliente;
import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Fornecedor;

@Controller
public class DashboardController {

  private final ProdutoUseCases produtoUseCases;

  public DashboardController(ProdutoUseCases produtoUseCases) {
    this.produtoUseCases = produtoUseCases;
  }

  @GetMapping("/dashboard")
  public String dashboard(Model model, Authentication authentication, @PageableDefault(size = 10) Pageable pageable) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    model.addAttribute("username", userDetails.getUsername());

    if (userDetails instanceof Cliente) {
      model.addAttribute("paginaDeProdutos", produtoUseCases.listarProdutosPublicos(null, pageable));
      return "dashboard-cliente"; // Retorna a página específica do cliente
    } else if (userDetails instanceof Fornecedor) {
      model.addAttribute("paginaDeProdutos", produtoUseCases.listarProdutosDoFornecedorLogado(userDetails, pageable));
      return "dashboard-fornecedor"; // Retorna a nova página do fornecedor
    }

    return "redirect:/login";
  }

  @GetMapping("fornecedor/produtos/cadastrar")
  public String paginaCadastrarProduto(Model model) {
    return "novo-produto";
  }

  @GetMapping("fornecedor/produtos/editar/{id}")
  public String paginaEditarProduto(@PathVariable UUID id, Model model) {
    model.addAttribute("produtoId", id);
    return "editar-produto";
  }
}