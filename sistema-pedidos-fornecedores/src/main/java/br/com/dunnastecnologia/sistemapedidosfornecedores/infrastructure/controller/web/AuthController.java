package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.controller.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
  Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

  @GetMapping("/login")
  public String showLoginPage() {
    if (authentication != null && authentication.isAuthenticated()
        && !"anonymousUser".equals(authentication.getPrincipal())) {
      return "redirect:/dashboard";
    }

    return "login";
  }
}