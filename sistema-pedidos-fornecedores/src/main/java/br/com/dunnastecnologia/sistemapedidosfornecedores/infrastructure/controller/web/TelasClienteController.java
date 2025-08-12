package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.controller.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases.ClienteUseCases;
import br.com.dunnastecnologia.sistemapedidosfornecedores.domain.model.Cliente;

@Controller
public class TelasClienteController {

  private final ClienteUseCases clienteUseCases;

  public TelasClienteController(ClienteUseCases clienteUseCases) {
    this.clienteUseCases = clienteUseCases;
  }

  @GetMapping("/cadastro-cliente")
  public String showCadastroClientePage() {
    return "cadastro-cliente";
  }

  @GetMapping("/carrinho")
  public String abrirCarrinho(Model model, Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    if (userDetails instanceof Cliente) {
      model.addAttribute("username", userDetails.getUsername());
      return "carrinho";
    }

    return "redirect:/login";
  }

  @GetMapping("/cliente/historico")
  public String telaHistorico() {
    return "historico-transacoes";
  }

  @GetMapping("/cliente/perfil")
  public String showPerfilCliente(Model model, Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    model.addAttribute("cliente", clienteUseCases.buscarClienteLogado(userDetails));
    return "perfil-cliente";
  }

  @GetMapping("/cliente/perfil/editar")
  public String showEditarPerfilCliente(Model model, Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    model.addAttribute("cliente", clienteUseCases.buscarClienteLogado(userDetails));
    return "perfil-cliente-editar";
  }
}
