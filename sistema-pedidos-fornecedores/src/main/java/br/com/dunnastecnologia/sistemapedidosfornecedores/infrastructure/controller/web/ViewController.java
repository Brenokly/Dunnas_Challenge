package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.controller.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null && authentication.isAuthenticated()
        && !"anonymousUser".equals(authentication.getPrincipal())) {
      return "redirect:/dashboard";
    }

    return "redirect:/login";
  }

  @GetMapping("/dashboard")
  public String dashboard() {
    return "dashboard";
  }

  @GetMapping("/cadastro-cliente")
  public String showCadastroClientePage() {
    return "cadastro-cliente";
  }
}