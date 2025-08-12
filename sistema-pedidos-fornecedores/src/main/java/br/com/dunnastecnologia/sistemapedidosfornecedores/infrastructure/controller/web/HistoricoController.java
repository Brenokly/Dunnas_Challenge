package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HistoricoController {
  @GetMapping("/cliente/historico")
  public String telaHistorico() {
    return "historico-transacoes";
  }
}
