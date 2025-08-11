package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.controller.web;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
      model.addAttribute("userType", "cliente");
      model.addAttribute("paginaDeProdutos", produtoUseCases.listarProdutosPublicos(null, pageable));
    } else if (userDetails instanceof Fornecedor) {
      model.addAttribute("userType", "fornecedor");
      model.addAttribute("paginaDeProdutos", produtoUseCases.listarProdutosDoFornecedorLogado(userDetails, pageable));
    }

    return "dashboard";
  }
}