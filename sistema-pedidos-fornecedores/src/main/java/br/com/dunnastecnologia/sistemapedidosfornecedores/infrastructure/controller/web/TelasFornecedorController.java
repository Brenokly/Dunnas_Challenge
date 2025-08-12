package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.controller.web;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases.FornecedorUseCases;

@Controller
public class TelasFornecedorController {

  // Mapeia todas as telas de fornecedor

  private final FornecedorUseCases fornecedorUseCases;

  public TelasFornecedorController(FornecedorUseCases fornecedorUseCases) {
    this.fornecedorUseCases = fornecedorUseCases;
  }

  @GetMapping("/cadastro-fornecedor")
  public String showCadastroFornecedorPage() {
    return "cadastro-fornecedor";
  }

  @GetMapping("/fornecedor/cupons/cadastrar")
  public String paginaCadastrarCupom() {
    return "cadastro-cupom";
  }

  @GetMapping("fornecedor/produtos/cadastrar")
  public String paginaCadastrarProduto(Model model) {
    return "cadastro-produto";
  }

  @GetMapping("/fornecedor/cupons")
  public String paginaGerenciarCupons() {
    return "gerenciar-cupons";
  }

  @GetMapping("fornecedor/produtos/editar/{id}")
  public String paginaEditarProduto(@PathVariable UUID id, Model model) {
    model.addAttribute("produtoId", id);
    return "editar-produto";
  }

  @GetMapping("/fornecedor/perfil")
  public String showPerfilFornecedor(Model model, Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    model.addAttribute("fornecedor", fornecedorUseCases.buscarFornecedorLogado(userDetails));
    return "perfil-fornecedor";
  }

  @GetMapping("/fornecedor/perfil/editar")
  public String showEditarPerfilFornecedor(Model model, Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    model.addAttribute("fornecedor", fornecedorUseCases.buscarFornecedorLogado(userDetails));
    return "perfil-fornecedor-editar";
  }
}
