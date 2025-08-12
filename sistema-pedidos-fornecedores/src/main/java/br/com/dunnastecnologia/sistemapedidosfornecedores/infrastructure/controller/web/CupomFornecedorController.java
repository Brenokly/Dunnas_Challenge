package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CupomFornecedorController {
  @GetMapping("/fornecedor/cupons")
  public String paginaGerenciarCupons() {
    return "gerenciar-cupons";
  }

  @GetMapping("/fornecedor/cupons/cadastrar")
  public String paginaCadastrarCupom() {
    return "novo-cupom";
  }
}
