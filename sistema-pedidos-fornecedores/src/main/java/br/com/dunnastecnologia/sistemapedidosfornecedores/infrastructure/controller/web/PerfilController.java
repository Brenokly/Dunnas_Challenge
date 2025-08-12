package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.controller.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases.ClienteUseCases;
import br.com.dunnastecnologia.sistemapedidosfornecedores.application.usecases.FornecedorUseCases;

@Controller
public class PerfilController {

  private final ClienteUseCases clienteUseCases;
  private final FornecedorUseCases fornecedorUseCases;

  public PerfilController(ClienteUseCases clienteUseCases, FornecedorUseCases fornecedorUseCases) {
    this.clienteUseCases = clienteUseCases;
    this.fornecedorUseCases = fornecedorUseCases;
  }

  @GetMapping("/cliente/perfil")
  public String showPerfilCliente(Model model, Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    model.addAttribute("cliente", clienteUseCases.buscarClienteLogado(userDetails));
    return "perfil-cliente";
  }

  @GetMapping("/fornecedor/perfil")
  public String showPerfilFornecedor(Model model, Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    model.addAttribute("fornecedor", fornecedorUseCases.buscarFornecedorLogado(userDetails));
    return "perfil-fornecedor";
  }

  @GetMapping("/cliente/perfil/editar")
  public String showEditarPerfilCliente(Model model, Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    model.addAttribute("cliente", clienteUseCases.buscarClienteLogado(userDetails));
    return "perfil-cliente-editar";
  }
}