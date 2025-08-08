package br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.dunnastecnologia.sistemapedidosfornecedores.infrastructure.repository.ClienteRepository;

@Service
public class AuthenticationService implements UserDetailsService {

    private final ClienteRepository clienteRepository;
    // private final FornecedorRepository fornecedorRepository;

    public AuthenticationService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
        // this.fornecedorRepository = fornecedorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = clienteRepository.findByUsuarioAndAtivoTrue(username).orElse(null);

        // ... (lógica futura para fornecedor) ...
        if (user != null) {
            return user;
        }

        throw new UsernameNotFoundException("Usuário não encontrado ou inativo: " + username);
    }
}
