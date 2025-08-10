package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

  @GetMapping("/login")
  public String showLoginPage() {
    return "login";
  }

  @GetMapping("/")
  public String home() {
    return "redirect:/login";
  }
}