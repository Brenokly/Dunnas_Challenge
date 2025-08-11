package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CadastroController {

  @GetMapping("/cadastro-cliente")
  public String showCadastroClientePage() {
    return "cadastro-cliente";
  }

}
